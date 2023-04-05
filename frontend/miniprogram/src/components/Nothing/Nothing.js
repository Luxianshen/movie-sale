import Taro, { Component } from '@tarojs/taro';
import { View, Image } from '@tarojs/components';
import nothing from "../../assets/images/nothing.png"
import "./Nothing.scss";
export default class Nothing extends Component {
  constructor(props) {
    super(props);
    this.state = {

    }
  }


  render() {
    const { data ,hideFlag} = this.props;
    return (
      <View class='nothing' hidden={hideFlag}>
        <Image src={nothing} class='nothing-img'></Image>
        <View class='nothing-text'>{data}</View>
      </View>

    )
  }
}
