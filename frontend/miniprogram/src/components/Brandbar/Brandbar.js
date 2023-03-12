import Taro, { Component } from '@tarojs/taro';
import { View, ScrollView, Text } from '@tarojs/components';
import "./Brandbar.scss";
export default class Brandbar extends Component {
  constructor(props) {
    super(props);
    this.state = {

    }
  }

  setArea(area){
    Taro.setStorageSync('area',area);
    this.$root.selectItem('brand');
  }


  render() {
    let data = this.props.data;
    return (
      <ScrollView
      scrollY
      style='height: 250Px;'
      scrollWithAnimation
      className={this.props.type =='brand'?'brandBar':'brandBar hide'}
      >
        {
          data.map((item,index)=>{
            return (
              <View className="brandItem" onClick={this.setArea.bind(this,item.areaName)}  key={item.id}>{item.areaName}<Text className="count"></Text></View>
            )
          })
        }
      </ScrollView>

    )
  }
}
