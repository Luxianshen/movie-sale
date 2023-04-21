import Taro, { useState, useEffect } from '@tarojs/taro';
import { View, Text } from '@tarojs/components';

function CountdownTimer() {
  const [remainingTime, setRemainingTime] = useState(15 * 60);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setRemainingTime(prevTime => prevTime - 1);
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);

  const minutes = Math.floor(remainingTime / 60);
  const seconds = remainingTime % 60;

  return (
    <View>
      <Text>{`${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`}</Text>
    </View>
  );
}