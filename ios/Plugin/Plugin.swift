import Foundation
import Vision
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(CapacitorOcr)
public class CapacitorOcr: CAPPlugin {
    @objc func detectText(_ call: CAPPluginCall) {
        if let filename = call.getString("filename") {
            let filePath = String(filename.dropFirst(7))

            guard let image = UIImage(contentsOfFile: filePath) else {
                call.reject("Could not load image from path")
                return;
            }

            TextDetector(call: call, image: image).detectText()

        } else if var base64 = call.getString("base64") {
            if let prefixRange = base64.range(of: "base64,") {
                base64 = String(base64[prefixRange.upperBound...])
            }

            guard let data = Data(base64Encoded: base64), let image = UIImage(data: data) else {
                call.reject("Could not load image from base64")
                return
            }

            TextDetector(call: call, image: image).detectText()

        } else {
            call.reject("Invalid image input")
        }
    }
}

public class TextDetector {
    var detectedText: [[String: Any]] = []
    let call: CAPPluginCall
    let image: UIImage
    var orientation: CGImagePropertyOrientation
    var detectedAlready = false

    public init(call: CAPPluginCall, image: UIImage) {
        self.call = call
        self.image = image
        self.orientation = CGImagePropertyOrientation.up
    }

    public func detectText() {
        // fail out if call is already used up
        guard !detectedAlready else {
            self.call.reject("An image has already been processed for text. Please instantiate a new TextDetector object.")
            return
        }
        self.detectedAlready = true

        guard let cgImage = image.cgImage else {
            print("Looks like uiImage is nil")
            return
        }

        let inputOrientation = call.getString("orientation")

        if inputOrientation != nil {
            orientation = self.getOrientation(orientation: inputOrientation!)
        } else {
            orientation = CGImagePropertyOrientation.up
        }

        // VNImageRequestHandler processes image analysis requests on a single image.
        let imageRequestHandler = VNImageRequestHandler(cgImage: cgImage, orientation: orientation, options: [:])

        DispatchQueue.global(qos: .userInitiated).async {
            do {
                try imageRequestHandler.perform([self.textDetectionRequest])
            } catch let error as NSError {
                print("Failed to perform image request: \(error)")
                self.call.reject(error.description)
            }
        }
    }

    lazy var textDetectionRequest: VNRecognizeTextRequest = {
        // Specifying the image analysis request to perform - text detection here
        let textDetectRequest = VNRecognizeTextRequest(completionHandler: handleDetectedText)
        return textDetectRequest
    }()

    func handleDetectedText(request: VNRequest?, error: Error?) {
        if error != nil {
            call.reject("Text Detection Error \(String(describing: error))")
            return
        }
        DispatchQueue.main.async {
            //  VNRecognizedTextObservation contains information about both the location and
            //  content of text and glyphs that Vision recognized in the input image.
            guard let results = request?.results as? [VNRecognizedTextObservation] else {
                self.call.reject("error")
                return
            }

            self.detectedText = results.map {[
                "topLeft": [Double($0.topLeft.x), Double($0.topLeft.y)] as [Double],
                "topRight": [Double($0.topRight.x), Double($0.topRight.y)] as [Double],
                "bottomLeft": [Double($0.bottomLeft.x), Double($0.bottomLeft.y)] as [Double],
                "bottomRight": [Double($0.bottomRight.x), Double($0.bottomRight.y)] as [Double],
                "text": $0.topCandidates(1).first?.string as String?
            ]}
            self.call.resolve(["textDetections": self.detectedText])
        }
    }

    func getOrientation(orientation: String) -> CGImagePropertyOrientation {
        switch orientation {
        case "UP": return CGImagePropertyOrientation.up
        case "DOWN": return CGImagePropertyOrientation.down
        case "LEFT": return CGImagePropertyOrientation.left
        case "RIGHT": return CGImagePropertyOrientation.right
        default:
            return CGImagePropertyOrientation.up
        }
    }
}
