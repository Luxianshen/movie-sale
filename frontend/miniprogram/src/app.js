import Taro, { Component } from "@tarojs/taro";
import Movies from "./pages/movies/movies";
import "./app.scss";
class App extends Component {
  config = {
    pages: [
      "pages/movies/movies",
      "pages/person/person",
      "pages/position/position",
      "pages/cinema/cinema",
      "pages/search/search",
      "pages/detail/detail",
      "pages/content/content",
      "pages/cinemaDetail/cinemaDetail",
      "pages/map/map",
      "pages/seat/seat",
      "pages/user/user",
      "pages/order/order",
      "pages/orderList/orderList"
    ],
    permission: {
      "scope.userLocation": {
        "desc": "你的位置信息将用于小程序位置接口的效果展示"
      }
    },
    requiredPrivateInfos: ["getLocation", "chooseLocation"],
    window: {
      backgroundTextStyle: "light",
      navigationBarBackgroundColor: "#e54847",
      navigationBarTitleText: "阿刘电影",
      navigationBarTextStyle: "white",
      enablePullDownRefresh: true
    },
    tabBar: {
      color: "#333",
      selectedColor: "#f03d37",
      backgroundColor: "#fff",
      borderStyle: "black",
      list: [
        {
          pagePath: "pages/movies/movies",
          text: "电影",
          iconPath: "./assets/images/index.png",
          selectedIconPath: "./assets/images/index_focus.png"
        },
        {
          pagePath: "pages/cinema/cinema",
          text: "影院",
          iconPath: "./assets/images/themeOld.png",
          selectedIconPath: "./assets/images/theme.png"
        },
        {
          pagePath: "pages/person/person",
          text: "我的",
          iconPath: "./assets/images/person.png",
          selectedIconPath: "./assets/images/personSelect.png"
        }
      ]
    }
  };

  componentDidMount() {}

  componentDidShow() {}

  componentDidHide() {}

  componentCatchError() {}

  render() {
    return <Movies />;
  }
}

Taro.render(<App />, document.getElementById("app"));
