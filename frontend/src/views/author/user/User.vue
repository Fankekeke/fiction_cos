<template>
  <a-row :gutter="20" style="width: 100%">
    <a-col :span="8">
      <a-card :loading="loading" :bordered="false">
        <a-form :form="form" layout="vertical">
          <a-row :gutter="20">
            <a-col :span="12">
              <a-form-item label='创作者姓名' v-bind="formItemLayout">
                <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入创作者姓名!' }] }
            ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='联系方式' v-bind="formItemLayout">
                <a-input v-decorator="[
            'phone',
            { rules: [{ required: true, message: '请输入联系方式!' }] }
            ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label='性别' v-bind="formItemLayout">
                <a-select v-decorator="[
              'sex',
              { rules: [{ required: true, message: '请输入性别!' }] }
              ]">
                  <a-select-option value="1">男</a-select-option>
                  <a-select-option value="2">女</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='备注' v-bind="formItemLayout">
                <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入备注!' }] }
            ]"/>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label='创作者头像' v-bind="formItemLayout">
                <a-upload
                  name="avatar"
                  action="http://127.0.0.1:9527/file/fileUpload/"
                  list-type="picture-card"
                  :file-list="fileList"
                  @preview="handlePreview"
                  @change="picHandleChange"
                >
                  <div v-if="fileList.length < 8">
                    <a-icon type="plus" />
                    <div class="ant-upload-text">
                      Upload
                    </div>
                  </div>
                </a-upload>
                <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                  <img alt="example" style="width: 100%" :src="previewImage" />
                </a-modal>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
          修改
        </a-button>
      </a-card>
    </a-col>
    <a-col :span="16">
      <div style="background:#ECECEC; padding:30px;margin-top: 30px">
        <a-list item-layout="vertical" size="large" :data-source="authorList">
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
              <a-col :span="6" v-for="(item1, index2) in item.bookInfoList" :key="index2" @click="selectBookDetailRate(item1.id)">
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
      </div>
    </a-col>
  </a-row>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'User',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      form: this.$form.createForm(this),
      formItemLayout,
      loading: false,
      courseInfo: [],
      authorList: [],
      dataLoading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      expertInfo: null
    }
  },
  mounted () {
    this.getExpertInfo(this.currentUser.userId)
  },
  methods: {
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.payDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: '✔'})
        }
      })
      return listData || []
    },
    getExpertInfo (userId) {
      this.dataLoading = true
      this.$get(`/cos/author-info/detailByUserId/${userId}`).then((r) => {
        this.expertInfo = r.data.data
        this.authorList = [r.data.data]
        console.log(this.expertInfo)
        this.setFormValues(this.expertInfo)
        // this.courseInfo = r.data.order
        this.dataLoading = false
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...expert}) {
      this.rowId = expert.id
      let fields = ['name', 'sex', 'phone', 'content']
      let obj = {}
      Object.keys(expert).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(expert['images'])
        }
        if (key === 'sex' && expert[key] != null) {
          expert[key] = expert[key].toString()
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = expert[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/author-info', {
            ...values
          }).then((r) => {
            this.$message.success('更新成功')
            this.loading = false
            this.getExpertInfo(this.currentUser.userId)
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
