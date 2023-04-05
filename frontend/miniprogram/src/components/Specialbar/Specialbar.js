import Taro, { Component } from '@tarojs/taro'
import { View, ScrollView, Text } from '@tarojs/components'
import "./Specialbar.scss"
export default class Specialbar extends Component {
  constructor(props) {
    super(props);
    this.state = {

    }
  }

  setArea(area){
    debugger
    Taro.setStorageSync('area',area);
    //this.$root.selectItem('special');
  };


  render() {
    const { data,showFlag }  = this.props;

    return (
      <View className='specialBar' hidden = {!showFlag} >
        <ScrollView scrollY style='height: 250Px;'  scrollWithAnimation  >
        {
                  data.map((item,index)=>{
                    return (
                      <View className="brandItem" onClick={this.setArea.bind(this,item.areaName)}  key={item.id}>{item.areaName}<Text className="count"></Text></View>
                    )
                  })
                }
        </ScrollView>
      </View>
    )
  }
}
