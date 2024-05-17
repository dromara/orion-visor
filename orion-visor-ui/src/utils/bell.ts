/**
 * 钟鸣
 * @param frequency 蜂鸣器频率Hz
 * @param duration 持续时间
 */
export const playBell = (frequency: number = 400, duration: number = .15) => {
  try {
    // 创建 AudioContext 对象
    const audioCtx = new (window.AudioContext)();
    const buffer = audioCtx.createBuffer(1, audioCtx.sampleRate, audioCtx.sampleRate);
    const channelData = buffer.getChannelData(0);
    // 获取正弦波形数据
    const sampleRate = audioCtx.sampleRate;
    const numSamples = Math.floor(duration * sampleRate);
    const sineWaveData = [];
    for (let i = 0; i < numSamples; ++i) {
      const time = i / sampleRate;
      // 计算当前样本点的值
      const value = Math.sin(2 * Math.PI * frequency * time);
      // 将值转换为-1到+1之间的范围内
      sineWaveData[i] = value > 0 ? value : -value;
    }
    // 复制正弦波形数据到buffer中
    for (let i = 0; i < sineWaveData.length; ++i) {
      channelData[i] = sineWaveData[i];
    }
    const source = audioCtx.createBufferSource();
    source.buffer = buffer;
    source.connect(audioCtx.destination);
    source.start();
  } catch (e) {
  }
};
