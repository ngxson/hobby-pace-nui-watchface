var Jimp = require('jimp');

function getImageLocations() {
  const DIST_FROM_TOP = 12;
  const IMG_DIM = 53;
  const ret = [];
  for (let i = 0; i < 60; i++) {
    const distanceBetween2Center = Math.round((320/2) - (IMG_DIM/2) + DIST_FROM_TOP);
    const rad = (i*(360/60) - 90) * (Math.PI/180);
    let x = Math.cos(rad) * distanceBetween2Center;
    let y = Math.sin(rad) * distanceBetween2Center;
    x = Math.round(x - IMG_DIM/2 + (320/2));
    y = Math.round(y - IMG_DIM/2 + (320/2));
    ret.push({i, x, y, deg: i*(360/60)});
  }
  return ret;
}

async function main() {
  const device = await Jimp.read('./device.png');
  const final = new Jimp(320, 320, 0xff000000);
  const spriteSet = new Jimp(53*60, 53, 0x00000000);

  final.blit(device, 0, 0);

  for (const {i, x, y, deg} of getImageLocations()) {
    const image = await Jimp.read('./minute_indicator.png');
    image.rotate(-deg, false);
    console.log(deg, x, y);

    final.blit(image, x, y);
    spriteSet.blit(image, 53*i, 0);
    //break;
  }

  final.write('./final.png');
  spriteSet.write('./sprite_set.png');
}
 
main();
console.log("Image Processing Completed");