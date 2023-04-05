import Taro, { Component } from '@tarojs/taro';
import { View, ScrollView, Text } from '@tarojs/components';
import "./Areabar.scss";
export default class Brandbar extends Component {
  constructor(props) {
    super(props);
    this.state = {

    }
  }

  selectItem(area){
    Taro.setStorageSync('nowArea',area);
    this.props.onClick && this.props.onClick('area');
  }


  render() {
    const { data,showFlag } = this.props;
    return (
    <View className='brandBar' hidden = {!showFlag}>
      <ScrollView scrollY style='height: 250Px;' scrollWithAnimation >
        {
          data.map((item,index)=>{
            return (
              <View className="brandItem" onClick={this.selectItem.bind(this,item.areaName)}  key={item.id}>{item.areaName}<Text className="count"></Text></View>
            )
          })
        }
      </ScrollView>
</View>
    )
  }
}
