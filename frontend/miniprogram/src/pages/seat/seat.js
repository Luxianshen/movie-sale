import Taro, {Component} from '@tarojs/taro'
import {View,Text,MovableView,MovableArea} from '@tarojs/components'
import './seat.scss'
export default class Seat extends Component {
  config = {
    enablePullDownRefresh: false
  }
  constructor(props) {
    super(props);
    this.state = {
      cinemaName:'',
      seatData: [],
      seatRow:[],
      seatRunTime: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,16,17,18,19,20],
      statusMap: {
        can: "https://p1.meituan.net/movie/9dfff6fd525a7119d44e5734ab0e9fb41244.png",
        No: "https://p1.meituan.net/movie/bdb0531259ae1188b9398520f9692cbd1249.png",
        select: "https://p0.meituan.net/movie/585588bd86828ed54eed828dcb89bfdd1401.png"
      },
      price: '0',
      active: '0',
      seatArray: [],
      buySeat: [],
      item:{}
    }

  }
  initParams() {
    this.state.buySeat=[];
    const params = this.$router.params;
    const cinemaName = params.cinemaName;
    const showId = params.showId;
    let item = JSON.parse(decodeURIComponent(params.item));

    this.setState({
      item: item,
      price: params.price,
      cinemaName: cinemaName
    })
    const self = this;
    Taro.setNavigationBarTitle({
      title: cinemaName
    })
    Taro.showLoading({
      title: "加载中..."
    });
    Taro.request({
      url: baseUrl + `/index/seat/${cinemaName}/${showId}`,
      method: 'get'
    }).then(res => {
      if (res.statusCode == 200) {
        Taro.hideLoading();

        const seatData = res.data.data;
        const seatArray = [];
        this.state.seatRow = [];
        this.state.seatRunTime.map(i => {
          let runData = seatData[i] ? seatData[i] : [];

          if (runData.length > 0) {
            let arr = [];
            let runNo = 0;
            runData.map((seat, seatTemp) => {

              let columnNo = seat["columnNo"] *1;
              if(columnNo > runNo+1){
                let run = columnNo- runNo-1;
                for(let i=0; i< run; i++){
                		arr.push('E')
                	}
                  runNo = columnNo;

              }else{
                runNo = runNo+1;
              }
                if (seat["status"] == "N") {
                  arr.push('0');
                } else {
                  arr.push('E')
                }

            })
            seatArray.push(arr);
            this.state.seatRow.push(i);
          }
        })
        self.setState({
          seatData: seatData,
          seatArray: seatArray
        });
      }
    })
  }
  selectSeat(row, column, item) {
    const self = this;
    const arr = this.state.seatArray;
    if (item == 0) {
      if (self.state.buySeat.length == 4) {
        Taro.showToast({
          title: '最多选择4个座位',
          duration: 2000
        })
        return false;
      } else {
        let buySeat = self.state.buySeat;
        arr[row][column] = '2';
        buySeat.push({
          "row": row,
          "column": column
        });
        self.setState({
          buySeat: buySeat,
          seatArray: arr
        })
      }
    } else {
      arr[row][column] = '0';
      const buySeat = this.state.buySeat;
      let tmpArr = this.state.buySeat;
      buySeat.map((value, index) => {
        if (value["row"] == row && value["column"] == column) {
          tmpArr.splice(index, 1);
          self.setState({
            buySeat: tmpArr,
            seatArray: arr
          })
        }
      })
    }

  }
  selectAll(seats) {
    const self = this;
    seats.map(item => {
      let row = parseInt(item.rowId.split('0')[0]);
      let column = parseInt(item.columnId.split('0')[0]);
      let itemIndex = self.state.seatArray[row][column];
      self.selectSeat(row, column, itemIndex);
    })

  }
  getRecomment(recomment, num) {
    switch (num) {
      case 1:
        this.selectAll(recomment.bestOne.seats);
        break;
      case 2:
        this.selectAll(recomment.bestTwo.seats);
        break;
      case 3:
        this.selectAll(recomment.bestThree.seats);
        break;
      case 4:
        this.selectAll(recomment.bestFour.seats);
        break;
    }
  }
  deleteBuy(item) {
    const row = item.row;
    const column = item.column;
    const status = this.state.seatArray[row][column];
    this.selectSeat(row, column, status);
  }
  navigate(url) {

    let item = this.state.item;
    let price = this.state.price;
    let buyNum = this.state.buySeat.length;
    let seatInfo = '';
    this.state.buySeat.map(item=>{
      seatInfo =  seatInfo + (item.row * 1 + 1) +'排' +item.column +'座;';
    })

    let cinemaName = this.state.cinemaName;
    url = url+`?cinemaName=${cinemaName}&buyNum=${buyNum}&price=${price}&seatInfo=${seatInfo}&item=${encodeURIComponent(JSON.stringify(item))}`
    Taro.navigateTo({
      url: url
    });
  }
  componentDidMount() {
    this.initParams();
  }

 render() {

    const show = this.state.item;
    const hall = this.state.seatData.hall;
    const movie = this.state.seatData.movie;
    const seatInfo = this.state.seatData ? this.state.seatData : [];
    const seatRow = this.state.seatRow;
    const seatTypeList = this.state.seatData.seat ? this.state.seatData.seat.seatTypeList : [];
    const seatMap = this.state.statusMap;
    const seatArray = this.state.seatArray;
    const recomment = this.state.seatData.seat ? this.state.seatData.seat.bestRecommendation : [];
    const price = Math.floor(this.state.price);
    return ( <
      View className = "selectSeat" >
      <
      View className = "header" >
      <
      View className = "title" > {
        show.filmName
      } < /View> <
      View className = "desc" >
      <
      Text className = "time" >  {
        show.showTime
      } < /Text> <
      Text classname = "lang" > < Text className = "language" > {
        show.language
      } < /Text><Text className="dim">{show.dim}</Text > < /Text> < /
      View > <
      /View>
      <View className = "seatCon" >
      <
      View className = "hallCon" >
      <
      View className = "hallName" > {
        hall.hallName
      } < /View> < /
      View >


  <MovableArea className = "seatMore">
     <View className = "rowList" > {seatRow.map((item, index) => {
          return (<View className = "numberId" key = {key} > {index + 1} < /View>)})} </View>
          
        <MovableView className = "Container" direction='all'>

        {Object.keys(seatArray).map(key => {
              return (
              <View className = "rowWrap" key = {key} > {
                  seatArray[key].map((item, index) => {
                      return (
                      <View className = "seatWrap" key = {index} > {item == '0' ?
                      < Image src = {seatMap.can}
                          onClick = {this.selectSeat.bind(this, key, index, item)} >
                           </Image>:(item == '2'?<Image src={seatMap.select}  onClick={this.selectSeat.bind(this,key,index,item)}></Image >
                          :<Text > < /Text>)}
                          < /View >
                        )
                      })
                  } </View>
                )
              })
          }
        </MovableView>
      </MovableArea>


      <View className = "seatMore" >
      <View class='alignLine'></View>
      <View className = "rowList" > {seatRow.map((item, index) => {
          return (
          <View className = "numberId" key = {key} > {index + 1} < /View>
          )})
      } </View>
      <View className = "Container" >
      {Object.keys(seatArray).map(key => {
            return (
            <View className = "rowWrap" key = {key} > {
                seatArray[key].map((item, index) => {
                    return (
                    <View className = "seatWrap" key = {index} > {item == '0' ?
                    < Image src = {seatMap.can}
                        onClick = {this.selectSeat.bind(this, key, index, item)} >
                         </Image>:(item == '2'?<Image src={seatMap.select}  onClick={this.selectSeat.bind(this,key,index,item)}></Image >
                        :<Text > < /Text>)}
                        < /View >
                      )
                    })
                } </View>
              )
            })
        }
        </View>
        < /View >

        </View>
        <View className = "type" > {
          seatTypeList.map((item, index) => {
            return (
            <View className = "item" key = {index} >
              <Image src = {item.icon} > < /Image>
              <Text className = "word" > {
                item.name
              } < /Text>
              < /View >
            )
          })
        } <
        /View> <
        View className = "comment" >
        <
        View className = "title" > 我的选择 < /View> <
        View className = "btn"
        className = {
          'hide btn'
        } >
        <
        View className = "btnItem"
        onClick = {
          this.getRecomment.bind(this, recomment, 1)
        } > 1 人 < /View> <
        View className = "btnItem"
        onClick = {
          this.getRecomment.bind(this, recomment, 2)
        } > 2 人 < /View> <
        View className = "btnItem"
        onClick = {
          this.getRecomment.bind(this, recomment, 3)
        } > 3 人 < /View> <
        View className = "btnItem"
        onClick = {
          this.getRecomment.bind(this, recomment, 4)
        } > 4 人 < /View> < /
        View > <
        View className = {
          this.state.buySeat.length == 0 ? 'btn hide' : 'btn'
        } > {
          this.state.buySeat.map((item, index) => {
            return ( <
              View className = "btnItem"
              key = {
                index
              }
              onClick = {
                this.deleteBuy.bind(this, item)
              } > {
                (item.row) * 1 + 1
              }
              排 {
                item.column
              }
              座 <
              /View>
            )
          })
        } <
        /View> < /
        View > <
        View className = {
          this.state.buySeat.length == 0 ? 'buyBtn' : 'hide buyBtn'
        } > 请先选座 < /View>

        <View className = {
          this.state.buySeat.length == 0 ? 'hide buyBtn' : 'buyBtn active'} onClick = {
          this.navigate.bind(this, '../order/order')} > ￥{this.state.buySeat.length * price}
        确认选座 < /View>
        < /View >
      );
    }
  }
