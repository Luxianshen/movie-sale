import Taro, { Component } from '@tarojs/taro';
import { View, ScrollView, Text } from '@tarojs/components';
import "./Brandbar.scss";
export default class Brandbar extends Component {
  constructor(props) {
    super(props);
    this.state = {

    }
  }

  selectItem(brand){
    Taro.setStorageSync('brand',brand);
    this.props.onClick && this.props.onClick('brand');
  }


  render() {
    const { data,showFlag } = this.props;
    return (
    <View className='brandBar' hidden = {!showFlag}>
      <ScrollView scrollY style='height: 250Px;' scrollWithAnimation >
        {
          data.map((item,index)=>{
            return (
              <View className="brandItem" onClick={this.selectItem.bind(this,item)}  key={index}>{item}<Text className="count"></Text></View>
            )
          })
        }
      </ScrollView>
</View>
    )
  }
}
