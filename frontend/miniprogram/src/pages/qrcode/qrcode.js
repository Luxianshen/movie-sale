import Taro, { Component } from '@tarojs/taro'
import { View,Text, Button,Video,CoverView,CoverImage } from '@tarojs/components'
import './qrcode.scss'
export default class Qrcode extends Component {
  render () {
    return (
      <View className='container'>
      <Video id='myVideo' src='src'>
        <CoverView className='controls'>
          <CoverView className='play' onClick='play'>
            <CoverImage className='img' src='https://storage.360buyimg.com/pubfree-bucket/taro-docs/c07c6984de/img/logo-taro.png' />
          </CoverView>
        </CoverView>
      </Video>
      </View>
    )
  }
}
