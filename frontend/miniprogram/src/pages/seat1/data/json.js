// 本地模拟json数据
var json = {
  
  "seatTypeList": [
    {
      "name": "可选",
      "type": "0",
      "seats": 1,
      "icon": "https://i.postimg.cc/BbbWyY5D/image.png",
      "isShow": "1",
      "position": "up"
    },
    {
      "name": "已选",
      "type": "0-1",
      "seats": 1,
      "icon": "https://i.postimg.cc/1X2dd93h/image.png",
      "isShow": "1",
      "position": "up"
    },
    {
      "name": "已售",
      "type": "0-2",
      "seats": 1,
      "icon": "https://i.postimg.cc/LXywzkds/image.png",
      "isShow": "1",
      "position": "up"
    },
    {
      "name": "维修",
      "type": "0-3",
      "seats": 1,
      "icon": "https://i.postimg.cc/BZVRbCcY/image.png",
      "isShow": "1",
      "position": "up"
    }
  ]
}

// 定义数据出口
module.exports = {
  dataList: json
}
