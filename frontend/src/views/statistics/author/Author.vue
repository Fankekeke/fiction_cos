<template>
  <a-card :bordered="false" class="card-area" style="padding: 20px">
    <div>
      <a-row :gutter="30">
        <a-col :span="24" style="padding:30px;margin-top: 30px;margin-bottom: 30px">
          <a-row :gutter="30">
            <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="authorList">
              <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                <template slot="actions">
                </template>
                <a-list-item-meta :description="item.content">
                  <a slot="title">
                    {{ item.name }}
                    <span style="margin-left: 15px;font-family: SimHei;font-size: 13px">粉丝<span style="">{{ item.fansNum }}</span></span>
                  </a>
                  <a-avatar slot="avatar" shape="square" :src="'http://127.0.0.1:9527/imagesWeb/' + item.images.split(',')[0]" v-if="item.images != null"/>
                  <a-avatar slot="avatar" shape="square" v-else/>
                </a-list-item-meta>
                <a-row :gutter="30">
                  <a-col :span="4" v-for="(item1, index2) in item.bookInfoList" :key="index2" @click="selectBookDetailRate(item1.id)">
                    <div style="background: #e8e8e8">
                      <a-carousel autoplay style="height: 280px;" v-if="item1.images !== undefined && item1.images !== ''">
                        <div style="width: 100%;height: 280px" v-for="(item2, index1) in item1.images.split(',')" :key="index1">
                          <img :src="'http://127.0.0.1:9527/imagesWeb/'+item2" style="width: 100%;height: 280px">
                        </div>
                      </a-carousel>
                      <a-card :bordered="false">
                        <span slot="title">
                          <span style="font-size: 14px;font-family: SimHei">
                            《{{ item1.name }}》 | {{ item1.tag }}
                            <div style="margin: 20px 0px 20px 0px">
                            </div>
                          </span>
                        </span>
                      </a-card>
                    </div>
                  </a-col>
                </a-row>
              </a-list-item>
            </a-list>
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
      pagination: {
        onChange: page => {
          console.log(page)
        },
        pageSize: 10
      },
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
      authorList: [],
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
      this.$get(`/cos/author-info/selectListDetail`).then((r) => {
        this.authorList = r.data.data
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
  height: 280px;
  line-height: 280px;
  overflow: hidden;
}

</style>
