import { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Image, Group, Arc, Rect, Shape } from 'react-konva';
import useImage from 'use-image';
import './App.css';
import { DEVICE_HEIGHT, DEVICE_WIDTH, MOON_SIZE_PX } from './Config';

//import srcTest from './assets/test.png';
import srcBase from './assets/base.png';
import srcMoon from './assets/moon_dim.png';
import srcHandH from './assets/hr.png';
import srcHandM from './assets/min.png';
import srcHandS from './assets/sec.png';


const SHOW_CLOCK = false;

function App() {
  const stageRef = useRef(null);

  const handleExport = () => {
    console.log(stageRef.current)
    const uri = stageRef.current.toDataURL();
    downloadURI(uri, `nui_${Date.now()}.png`);
  };

  const [time, setTime] = useState(getTime());
  useEffect(() => {
    const intervalId = setInterval(() => {
      setTime(getTime());
    }, 1000);
    return () => clearInterval(intervalId);
  }, []);
  const {h, m, s} = time;

  return (
    <div className="App">
      <Stage height={DEVICE_HEIGHT} width={DEVICE_WIDTH} ref={stageRef}>
        <Layer>
          <MImage src={srcBase} />
          <MoonClipped dayOfMonth={s} />
        </Layer>
        {SHOW_CLOCK && <Layer>
          <MImage
            src={srcHandH} rotation={(360 * h / 12) + (30 * m / 60)}
            offsetX={DEVICE_WIDTH / 2} x={DEVICE_WIDTH / 2}
            offsetY={DEVICE_WIDTH / 2} y={DEVICE_WIDTH / 2}
          />
          <MImage
            src={srcHandM} rotation={360 * m / 60}
            offsetX={DEVICE_WIDTH / 2} x={DEVICE_WIDTH / 2}
            offsetY={DEVICE_WIDTH / 2} y={DEVICE_WIDTH / 2}
          />
          <MImage
            src={srcHandS} rotation={360 * s / 60}
            offsetX={DEVICE_WIDTH / 2} x={DEVICE_WIDTH / 2}
            offsetY={DEVICE_WIDTH / 2} y={DEVICE_WIDTH / 2}
          />
        </Layer>}
      </Stage>
      <br/>
      <br/>
      <button onClick={handleExport}>Export</button>
      <br/>
      <br/>
      <span style={{color: 'white'}}>{s % 30}</span>
    </div>
  );
}

const LOOKUP_TLB = [
    0,   6,  12,  18,  24,  // 5
   30,  36,  42,  48,  54,  // 10
   60,  66,  72,  78,  90,  // 15
   90,  96, 102, 108, 114,  // 20
  120, 126, 132, 138, 144,  // 25
  150, 156, 162, 168, 174,  // 30
];
const DEBUG = false;
const degToRad = d => d * Math.PI / 180;
function MoonClipped({dayOfMonth}) {
  const x = DEVICE_WIDTH / 2 - MOON_SIZE_PX / 2;
  const y = 70;
  const shadow = 30;
  const _clippingDeg = LOOKUP_TLB[dayOfMonth % 30];
  const clippingDeg = (_clippingDeg > 90) ? (_clippingDeg + 180 - 6) : _clippingDeg;
  //const maskSizeCoef = Math.sin((dayOfMonth - 3.75) * Math.PI * 4 / 30) * 1 + 2;
  //console.log(maskSizeCoef)
  const maskSizeCoef = 1.1;
  const maskSize = MOON_SIZE_PX * maskSizeCoef;
  const halfSize = MOON_SIZE_PX / 2;

  const maskOffset = (maskSize - MOON_SIZE_PX) / 2;
  const distanceToCenterX = Math.sin(degToRad(clippingDeg)) * (MOON_SIZE_PX + maskOffset);
  const clippingX = DEVICE_HEIGHT / 2 - distanceToCenterX;
  const clippingY = y + maskSize / 2 - maskOffset;

  return <>
    {/* <Group clipFunc={DEBUG ? null : (ctx) => {
      ctx.beginPath();
      ctx.arc(x + halfSize, y + halfSize, halfSize, 0, Math.PI * 2, false);
      ctx.fill();
    }}>
      <MImage src={srcBase} />
      <MImage src={srcMoon} x={x} y={y} />
      {[0, 1, 2].map(i => <Arc
        key={i}
        x={clippingX}
        y={clippingY}
        height={maskSize}
        width={maskSize}
        angle={360}
        fill={DEBUG ? 'white' : 'black'}
        shadowBlur={shadow}
        shadowColor="black"
        shadowOpacity={1}
      />)}
    </Group> */}

    <Group clipFunc={DEBUG ? null : (ctx) => {
      ctx.beginPath();
      ctx.arc(x + halfSize, y + halfSize, halfSize, 0, Math.PI * 2, false);
      ctx.fill();
    }}>
      <MImage src={srcBase} />
      <Group clipFunc={(ctx) => {
        ctx.save();
        ctx.beginPath();
        ctx.arc(clippingX, clippingY, maskSize / 2, 0, Math.PI * 2, false);
        ctx.restore();
      }}>
        <MImage src={srcBase} />
        <MImage src={srcMoon} x={x} y={y} />
        <Shape
          sceneFunc={(ctx) => {
            ctx.fillStyle = 'black';
            [0, 1, 2].map(() => {
              ctx.beginPath();
              ctx.arc(clippingX, clippingY, maskSize / 2, 0, Math.PI * 2, false);
              ctx.rect(400, 0, -400, 400);
              ctx.shadowBlur = shadow;
              ctx.shadowColor = 'black';
              ctx.shadowOpacity = 1;
              ctx.fill();
            });
          }}
        />
      </Group>
    </Group>

    {/* <Group clipFunc={(ctx) => {
      ctx.beginPath();
      ctx.arc(clippingX, clippingY, maskSize / 2, 0, Math.PI * 2, false);
      ctx.fill();
    }}>
      <MImage src={srcBase} />
      {DEBUG && <TestMatte />}
    </Group> */}
  </>;
}

const TestMatte = () => <Rect x={0} y={0} height={DEVICE_HEIGHT} width={DEVICE_WIDTH} fill="blue" />;

function MImage({src, ...props}) {
  const [image] = useImage(src);
  return <Image image={image} {...props} />;
}

function downloadURI(uri, name) {
  var link = document.createElement('a');
  link.download = name;
  link.href = uri;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

function getTime() {
  const d = new Date();
  return {
    h: d.getHours() % 12,
    m: d.getMinutes(),
    s: d.getSeconds(),
  }
}

export default App;
