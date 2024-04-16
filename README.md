<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Image To Text</h3>
<p align="center"><strong><code>@capacitor-community/image-to-text</code></strong></p>
<p align="center">
  Capacitor plugin for image to text processing using Apple's Vision Framework for iOS and MLKit's Vision Framework for Android.</a>.
</p>

## Credits

This project was forked from the [Cap ML](https://github.com/bendyworks/cap-ml) plugin written by Vennela Kodali. It was refactored and converted to Capacitor 4.

- For Capacitor 4 projects use v4.x
- For Capacitor 5 projects use v5.x
- For Capacitor 6 projects use v6.x

## Installation

```
npm install @capacitor-community/image-to-text
```

## Usage

There is one method `detectText` that takes a filename of an image and will return the text associated with it.

Add the following to your application:

```typescript
import { Ocr, TextDetections } from '@capacitor-community/image-to-text';

...

const data: TextDetections = await Ocr.detectText({ filename: '[get-filename-of-image-jpg]' });
for (let detection of data.textDetections) {
    console.log(detection.text);
}
```

The above code will convert the image file and `console.log` the text found in it.

## Example with Camera

You can use the [`@capacitor/camera`](https://capacitorjs.com/docs/apis/camera) plugin to take a photo and convert it to text:

```
import { Camera, CameraResultType, CameraSource } from '@capacitor/camera';
import { Ocr, TextDetections } from '@capacitor-community/image-to-text';
```

...

```typescript
const photo = await Camera.getPhoto({
  quality: 90,
  allowEditing: true,
  resultType: CameraResultType.Uri,
  source: CameraSource.Camera,
});

const data: TextDetections = await Ocr.detectText({ filename: photo.path });

for (let detection of data.textDetections) {
  console.log(detection.text);
}
```

A full sample application can be found [here](https://github.com/dtarnawsky/capacitor-ocr-example).

![video of scanning a card and it being converted to text](https://user-images.githubusercontent.com/84595830/221210852-2203919a-bb43-46ed-b9bf-04d096425dcb.gif)

## iOS Usage

No additional setup is required to use this plugin in a iOS Capacitor project.

## Android Usage

Your project must include a `google-services.json` file stored in the Android project folder (usually `android/app`).

### Create Firebase Project and App

- Sign in to [console.firebase.google.com](https://console.firebase.google.com/)
- Click on `Add Project` and follow through the steps.
- Click the `Android` icon to create an android app.
- Enter the `Package Name` which must match your apps package name (You can find it in `android/app/AndroidManifest.xml`).
- Click `Register App`
- Download `google-services.json` and save into your project's `android/app` directory.

### Add Firebase SDK

The sample project has this in place in its `build.gradle` (see [here as a reference](https://github.com/dtarnawsky/capacitor-ocr-example/blob/09e7fb935f68e642d4906eb0ed002a52d1868b52/android/app/build.gradle#L47)).

Note: Most starter Capacitor projects are preconfigured to load `google-services.json`.

## API Reference

<docgen-index>

* [`detectText(...)`](#detecttext)
* [Interfaces](#interfaces)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### detectText(...)

```typescript
detectText(options: DetectTextFileOptions | DetectTextBase64Options) => Promise<TextDetections>
```

Detect text in an image

| Param         | Type                                                                                                                                      | Description                |
| ------------- | ----------------------------------------------------------------------------------------------------------------------------------------- | -------------------------- |
| **`options`** | <code><a href="#detecttextfileoptions">DetectTextFileOptions</a> \| <a href="#detecttextbase64options">DetectTextBase64Options</a></code> | Options for text detection |

**Returns:** <code>Promise&lt;<a href="#textdetections">TextDetections</a>&gt;</code>

--------------------


### Interfaces


#### TextDetections

| Prop                 | Type                         |
| -------------------- | ---------------------------- |
| **`textDetections`** | <code>TextDetection[]</code> |


#### TextDetection

| Prop              | Type                          |
| ----------------- | ----------------------------- |
| **`bottomLeft`**  | <code>[number, number]</code> |
| **`bottomRight`** | <code>[number, number]</code> |
| **`topLeft`**     | <code>[number, number]</code> |
| **`topRight`**    | <code>[number, number]</code> |
| **`text`**        | <code>string</code>           |


#### DetectTextFileOptions

| Prop              | Type                                                          |
| ----------------- | ------------------------------------------------------------- |
| **`filename`**    | <code>string</code>                                           |
| **`orientation`** | <code><a href="#imageorientation">ImageOrientation</a></code> |


#### DetectTextBase64Options

| Prop              | Type                                                          |
| ----------------- | ------------------------------------------------------------- |
| **`base64`**      | <code>string</code>                                           |
| **`orientation`** | <code><a href="#imageorientation">ImageOrientation</a></code> |


### Enums


#### ImageOrientation

| Members     | Value                |
| ----------- | -------------------- |
| **`Up`**    | <code>'UP'</code>    |
| **`Down`**  | <code>'DOWN'</code>  |
| **`Left`**  | <code>'LEFT'</code>  |
| **`Right`** | <code>'RIGHT'</code> |

</docgen-api>

## Compatibility

Images are expected to be in portrait mode only, i.e. with text facing up. It will try to process even otherwise, but note that it might result in gibberish.

iOS and Android are supported. Web is not.

| Feature                          | ios                         | android                                                                                                              |
| -------------------------------- | --------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| ML Framework                     | CoreML Vision               | Firebase MLKit                                                                                                       |
| Text Detection with Still Images | Yes                         | Yes                                                                                                                  |
| Detects lines of text            | Yes                         | Yes                                                                                                                  |
| Bounding Coordinates for Text    | Yes                         | Yes                                                                                                                  |
| Image Orientation                | Yes (Up, Left, Right, Down) | Yes (Up, Left, Right, Down)                                                                                          |
| Skewed Text                      | Yes                         | Unreliable                                                                                                           |
| Rotated Text (<~ 45deg)          | Yes                         | Yes (but with noise)                                                                                                 |
| On-Device                        | Yes                         | Yes                                                                                                                  |
| SDK/ios Version                  | ios 13.0 or newer           | Targets API level >= 16<br>Uses Gradle >= 4.1<br>com.android.tools.build:gradle >= v3.2.1<br>compileSdkVersion >= 28 |
|                                  |                             |                                                                                                                      |

## License

Hippocratic License Version 2.0.

For more information, refer to LICENSE file
