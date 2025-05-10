<template>
  <a-card :bordered="false" class="card-area" style="padding: 20px">
    <div>
      <a-row :gutter="30">
        <a-col :span="24" style="padding:30px;margin-top: 30px;margin-bottom: 30px">
          <a-row :gutter="30">
            <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="bookList">
              <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                <template slot="actions">
                  <span>
                    浏览量 <span style="color: #fa541c">{{ item.views }}</span> 次
                  </span>
                </template>
                <img
                  slot="extra"
                  width="150"
                  alt="logo"
                  :src="'http://127.0.0.1:9527/imagesWeb/' + item.images.split(',')[0]"
                />
                <a-list-item-meta :description="'作者- ' + item.authorInfo.name">
                  <a slot="title">
                    《{{ item.name }}》
                    <span style="margin-left: 15px;font-family: SimHei">排名<span style="">{{ index + 1 }}</span></span>
                  </a>
                  <a-avatar slot="avatar" shape="square" :src="'http://127.0.0.1:9527/imagesWeb/' + item.authorInfo.images.split(',')[0]" />
                </a-list-item-meta>
                {{ item.content }}
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
      this.$get(`/cos/book-info/selectListTop`).then((r) => {
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
