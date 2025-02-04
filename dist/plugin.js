var capacitorCapacitorOcr = (function (exports, core) {
    'use strict';

    exports.ImageOrientation = void 0;
    (function (ImageOrientation) {
        ImageOrientation["Up"] = "UP";
        ImageOrientation["Down"] = "DOWN";
        ImageOrientation["Left"] = "LEFT";
        ImageOrientation["Right"] = "RIGHT";
    })(exports.ImageOrientation || (exports.ImageOrientation = {}));

    const Ocr = core.registerPlugin('CapacitorOcr', {});

    exports.Ocr = Ocr;

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
