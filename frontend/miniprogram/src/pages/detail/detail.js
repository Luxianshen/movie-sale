import Taro, {
  Component
} from '@tarojs/taro'
import {
  View,
  Image
} from '@tarojs/components'
import Selectbar from "../../components/Selectbar/Selectbar";
import Brandbar from "../../components/Brandbar/Brandbar";
import Specialbar from "../../components/Specialbar/Specialbar";
import './detail.scss'

export default class Detail extends Component {
  config = {
    enablePullDownRefresh: false
  }
  constructor(props) {
    super(props);
    this.state = {
      active: 0,
      params: {},
      offset: 0,
      hide: false,
      type: '',
      flag: true,
      showDate: "",
      detailMovie: {},
      dates: [],
      cinemas: [],
      allData: {},
      scrollLeft: '0',
    }
  }
  componentWillMount() {
    let params = this.$router.params;
    let title = params.title;
    Taro.setNavigationBarTitle({
      title: title
    })
    this.setState({
      params: {
        title: params.title,
        id: params.id,
        cityId: params.cityId
      }
    });
  }

  componentDidMount() {
    this.getDetailData();
  }
  getfilterCinemas() {
    let cityId = this.state.params.cityId;
    Taro.request({
      url: `http://localhost:8080/index/schedule/123/${this.state.params.id}/2023-03-04` //${this.state.showDate}
    }).then(res => {
      if (res.statusCode == 200) {
        let data = res.data.data;
        this.setState({
          allData: data,
          cinemas: data.list
        });
      }
    })
  }
  getDetailData() {
    Taro.request({
      url: `http://localhost:8080/index/movieDetail/123/${this.state.params.id}`
    }).then(res => {
      if (res.statusCode == 200) {
        debugger
        let data = res.data.data;
        let movie = data.movie;
        let showDate = data.dateList[0];
        this.getHotDate(showDate);
        this.setState({
          detailMovie: movie,
          dates: data.dateList
        }, () => {
          this.getfilterCinemas()
        });
      }
    }).catch(err => {
      console.log(err.message);
    })
  }
  getDate() {
    let date = new Date();
    let dateString = "";
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    month = month >= 10 ? month : '0' + month
    let day = date.getDate();
    day = day >= 10 ? day : '0' + day;
    dateString = year + "-" + month + "-" + day;
    return dateString;
  }
  getHotDate(showDate) {
    let now = new Date();
    if (new Date(showDate) < now) {
      showDate = this.getDate();
      this.setState({
        showDate: showDate
      });
    }
    Taro.showLoading({
      title: "加载数据中"
    });
    Taro.request({
      url: `http://localhost:8080/index/query/440100`,
      method: 'GET',
      data: {
        movieId: this.state.params.id,
        day: showDate,
        offset: this.state.offset,
        limit: 20,
        districtId: -1,
        lineId: -1,
        hallType: -1,
        brandId: -1,
        serviceId: -1,
        areaId: -1,
        stationId: -1,
        item: "",
        updateShowDay: true,
        reqId: Date.now(),
        cityId: this.state.params.cityId,
      }
    }).then(res => {
      if (res.statusCode == 200) {
        let self = this
        Taro.hideLoading();
        let data = res.data.data;
        let cinemas = this.state.cinemas;
        let showDatas = [];
        this.state.dates.map((item) => {
          let str = item;
          item = this.formatDateString(str);
          showDatas.push(item);
        })
        this.setState({
          dates: showDatas,
          cinemas: self.state.cinemas.concat(cinemas)
        }, () => {});
      }
    })
  }
  formatDateString(date) {
    let dates = new Date();
    let day = dates.getDate();
    let dateParam = date.split('-');
    if (dateParam[2] == day) {
      return "今天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else if (dateParam[2] == day + 1) {
      return "明天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else if (dateParam[2] == day + 2) {
      return "后天" + dateParam[1] + "月" + dateParam[2] + "日";
    } else {
      let weekday = new Date(date).getDay();
      let arr = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"]
      return arr[weekday] + dateParam[1] + "月" + dateParam[2] + "日";
    }
  }
  selectDate(item, index, e) {
    this.setState({
      active: index,
      offset: 0,
      showDate: item.date,
      scrollLeft: e.target.offsetLeft
    }, () => {
      this.getAddrByDay(item)
    });
  }
  getAddrByDay(item) {
    Taro.showLoading({
      title: "加载数据中"
    });
    debugger
    Taro.request({
      url: `http://localhost:8080/index/schedule/123/${this.state.params.id}/${this.state.showDate}`
    }).then(res => {
      if (res.statusCode == 200) {
        Taro.hideLoading();
        let data = res.data.data;
        this.setState({
          cinemas: data.list
        });
      }
    })
  }
  load(e) {
    let offset = this.state.offset
    offset = offset + 20;
    let showDate = this.state.showDate;
    this.setState({
      offset: offset
    }, () => {
      this.getHotDate(showDate);
    })
  }
  scroll(e) {
    if (e.detail.scrollTop >= 185 && this.state.flag) {
      let hide = true;
      this.setState({
        hide: hide,
        flag: false
      })
    }
    if (e.detail.scrollTop < 185 && !this.state.flag) {
      let hide = false;
      this.setState({
        hide: hide,
        flag: true
      })
    }
  }
  showArea(name, e) {
    e.stopPropagation();
    e.preventDefault();
    if (this.state.hide) {
      this.setState({
        hide: false,
        type: ''
      });
    } else {
      this.setState({
        hide: true,
        type: name
      });
    }
  }
  navigateContent(url, item) {
    let cityId = this.state.cityId;
    url = url + `?id=${item.id}&title=${item.nm}&cityId=${cityId}`
    Taro.navigateTo({
      url: url
    })
  }
  navigateToCinema(url, item) {
    url = url + `?cinemaId=${item.cinemaId}&movieId=${this.state.params.id}`;
    Taro.navigateTo({
      url: url
    })
  }
  render() {
    let itemData = this.state.detailMovie;
    let cinemas = this.state.cinemas;
    return ( <
        ScrollView className = "detailContainer"
        scrollY scrollWithAnimation scrollTop = '0'
        style = 'height: 100vh;'
        lowerThreshold = '20'
        upperThreshold = '20'
        onScrolltolower = {
          this.load
        }
        onScroll = {
          this.scroll
        } >
        <
        View className = {
          this.state.hide ? 'hide detailBox' : 'detailBox'
        } >
        <
        View className = "bg" >
        <
        Image src = {
          itemData.backgroundPicture
        } > < /Image> <
        view className = "blurBg" > < /view> <
        View className = "detailContent" >
        <
        Image className = "poster"
        src = {
          itemData.poster
        } > < /Image> <
        View className = "detailInfo" >
        <
        View className = "title" > {
          itemData.showName
        } < /View> <
        View className = "star" > {
          itemData.leadingRole
        } < /View> {
        itemData.globalReleased ? < View className = "comment" > 观众评 {
          itemData.remakr
        } < /View>:<View className="comment">{itemData.wish}人想看</View >
      } <
      View className = "type" > {
        itemData.cat
      } < /View> <
    View className = "hours" > {
      itemData.country
    }
    /{itemData.duration}分钟</View >
    <
    View className = "time" > {
        itemData.pubDesc
      } < /View> < /
      View > <
      View className = "arrow"
    onClick = {
        this.navigateContent.bind(this, '../content/content', itemData)
      } >
      <
      View className = "icon" > < /View> < /
      View > <
      /View> < /
      View > <
      /View> <
    View className = {
        this.state.hide ? 'fix' : ''
      } >
      <
      ScrollView className = 'dateSelect'
    scrollX scrollWithAnimation scrollTop = '0'
    scrollLeft = {
      this.state.scrollLeft
    } > {
      this.state.dates.map((item, index) => {
          return ( < View className = {
              this.state.active == index ? 'active scroll-item' : 'scroll-item'
            }
            key = {
              index
            }
            onClick = {
              this.selectDate.bind(this, item, index)
            } > {
              item
            } < /View>)
          })
      } <
      /ScrollView> <
      View className = "dateSelect" >
      <
      View className = {
        this.state.type == 'city' ? 'scroll-item itemActive' : 'scroll-item '
      }
      onClick = {
        this.showArea.bind(this, 'city')
      } > 全城 < View className = "arrow" > < /View></View >
      <
      View className = "line" > | < /View> <
      View className = {
        this.state.type == 'brand' ? 'scroll-item itemActive' : 'scroll-item'
      }
      onClick = {
        this.showArea.bind(this, 'brand')
      } > 品牌 < View className = "arrow" > < /View></View >
      <
      View className = "line" > | < /View> <
      View className = {
        this.state.type == 'special' ? 'scroll-item itemActive' : 'scroll-item'
      }
      onClick = {
        this.showArea.bind(this, 'special')
      } > 特色 < View className = "arrow" > < /View></View >
      <
      Selectbar data = {
        this.state.allData
      }
      type = {
        this.state.type
      }
      /> <
      Specialbar data = {
        this.state.allData
      }
      type = {
        this.state.type
      }
      /> <
      Brandbar data = {
        this.state.allData
      }
      type = {
        this.state.type
      }
      /> < /
      View > <
      /View> <
      View className = "cinemas" > {
        cinemas.map(item => {
          return ( <
            View className = "cinemasItem"
            key = {
              item.cinemaId
            }
            onClick = {
              this.navigateToCinema.bind(this, "../cinemaDetail/cinemaDetail", item)
            } >
            <
            View className = "leftCinemas" >
            <
            View className = "cinemaName" > {
              item.cinemaName
            } < Text className = "price" > {
              item.minPrice
            } < /Text><Text className="smallText">元起</Text > < /View> <
            View className = "cinemaAddr" > {
              item.address
            } < /View> < /
            View > <
            View className = "cinemasDis" > {
              item.distance
            } < /View> < /
            View >
          )
        })
      } <
      /View> < /
      ScrollView >
    )
  }
}
