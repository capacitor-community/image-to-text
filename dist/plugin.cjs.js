'use strict';

var core = require('@capacitor/core');

exports.ImageOrientation = void 0;
(function (ImageOrientation) {
    ImageOrientation["Up"] = "UP";
    ImageOrientation["Down"] = "DOWN";
    ImageOrientation["Left"] = "LEFT";
    ImageOrientation["Right"] = "RIGHT";
})(exports.ImageOrientation || (exports.ImageOrientation = {}));

const Ocr = core.registerPlugin('CapacitorOcr', {});

exports.Ocr = Ocr;
//# sourceMappingURL=plugin.cjs.js.map
