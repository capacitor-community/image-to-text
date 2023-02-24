'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

exports.ImageOrientation = void 0;
(function (ImageOrientation) {
    ImageOrientation["Up"] = "UP";
    ImageOrientation["Down"] = "DOWN";
    ImageOrientation["Left"] = "LEFT";
    ImageOrientation["Right"] = "RIGHT";
})(exports.ImageOrientation || (exports.ImageOrientation = {}));

const CapacitorOcr = core.registerPlugin('CapacitorOcr', {});
// export * from './web';

exports.CapacitorOcr = CapacitorOcr;
//# sourceMappingURL=plugin.cjs.js.map