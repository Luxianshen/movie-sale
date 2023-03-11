import Taro, { Component } from '@tarojs/taro'
import { View, ScrollView, Text } from '@tarojs/components'
import "./Specialbar.scss"
export default class Specialbar extends Component {
  constructor(props) {
    super(props);
    this.state = {

    }
  }
  setBrand(brand){
    Taro.setStorageSync('brand',brand);
    console.log(brand);
  };
  navigateCinema(e){
    wx.switchTab({
        url: '/pages/cinema/cinema'
    })
  }


  render() {
    let data = this.props.data;
    return (
      <View className={this.props.type =='special'?'specialBar':'specialBar hide'}>
        <ScrollView
        scrollY
        style='height: 250Px;'
        scrollWithAnimation
        >
          {
            data.map((item,index)=>{
              return (
                <View className="brandItem" onClick={this.setBrand.bind(this,item)} key={index}>{item}<Text className="count"></Text></View>
              )
            })
          }
        </ScrollView>
        <View className="selectCertian">
            <View className="reset">重置</View>
            <View className="certain" onClick={this.navigateCinema.bind(this,e)}>确定</View>
        </View>
      </View>
    )
  }
}
