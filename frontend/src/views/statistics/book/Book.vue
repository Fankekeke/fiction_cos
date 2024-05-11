<template>
  <a-card :bordered="false" class="card-area">
    <div>
      <a-card hoverable :bordered="false" style="width: 100%">
        <a-skeleton active v-if="loading" />
        <apexchart  v-if="!loading" type="bar" height="300" :options="chartOptions" :series="series"></apexchart>
      </a-card>
      <a-row :gutter="30">
        <a-col :span="24" style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
          <a-row :gutter="30">
            <a-col :span="6" v-for="(item, index) in bookList" :key="index" @click="selectBookDetailRate(item.id)">
              <div style="background: #e8e8e8">
                <a-carousel autoplay style="height: 350px;" v-if="item.images !== undefined && item.images !== ''">
                  <div style="width: 100%;height: 350px" v-for="(item, index) in item.images.split(',')" :key="index">
                    <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 350px">
                  </div>
                </a-carousel>
                <a-card :bordered="false">
              <span slot="title">
                <span style="font-size: 14px;font-family: SimHei">
                  {{ item.name }} | {{ item.tag }}
                  <div style="margin: 20px 0px 20px 0px">
                    <a-avatar shape="square" :src="'http://127.0.0.1:9527/imagesWeb/' + item.authorInfo.images.split(',')[0]" />
                    <span style="font-size: 12px;font-family: SimHei">{{ item.authorInfo.name }}</span>
                    <span style="font-size: 12px;font-family: SimHei;float: right">{{ item.updateDate }}</span>
                  </div>
                </span>
              </span>
                </a-card>
              </div>
            </a-col>
          </a-row>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>
export default {
  name: 'Work',
  data () {
    return {
      series: [{
        name: '浏览量',
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'line',
          height: 300
        },
        xaxis: {
          categories: []
        },
        stroke: {
          curve: 'stepline'
        },
        dataLabels: {
          enabled: false
        },
        title: {
          text: '章节流量统计',
          align: 'left'
        },
        markers: {
          hover: {
            sizeOffset: 4
          }
        }
      },
      bookList: [],
      loading: false
    }
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
    selectBookDetailRate (bookId) {
      this.loading = true
      this.$get(`/cos/book-detail-info/rate`, {bookId}).then((r) => {
        this.series[0].data = r.data.data.map(obj => { return obj.views })
        this.chartOptions.xaxis.categories = r.data.data.map(obj => { return obj.name })
        setTimeout(() => {
          this.loading = false
        }, 200)
      })
    },
    getWorkStatusList () {
      this.$get(`/cos/book-info/selectListDetail`).then((r) => {
        this.bookList = r.data.data
      })
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 350px;
  line-height: 350px;
  overflow: hidden;
}

</style>
