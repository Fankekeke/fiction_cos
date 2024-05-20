<template>
  <a-drawer
    :maskClosable="false"
    width="100%"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    wrapClassName="aa"
    :getContainer="false"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="6">
          <div style="font-size: 13px;font-family: SimHei" v-if="bookData !== null">
            <div style="background: #e8e8e8;box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;">
              <a-carousel autoplay style="height: 350px;" v-if="bookData.images !== undefined && bookData.images !== ''">
                <div style="width: 100%;height: 350px" v-for="(item, index) in bookData.images.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 350px">
                </div>
              </a-carousel>
              <a-card :bordered="false">
              <span slot="title">
                <span style="font-size: 14px;font-family: SimHei">
                  {{ bookData.name }} | {{ bookData.tag }}
                  <div style="margin-top: 10px">{{ bookData.content }}</div>
                  <div style="margin-top: 10px">最后更新时间：{{ bookData.updateDate ? bookData.updateDate : '- -' }}</div>
                  <div style="margin-top: 10px">最后章节：{{ bookData.lastChapter ? bookData.lastChapter : '- -' }}</div>
                  <div style="margin: 20px 0px 20px 0px">
                    <a-avatar shape="square" :src="'http://127.0.0.1:9527/imagesWeb/' + bookData.authorInfo.images.split(',')[0]" />
                    <span style="font-size: 12px;font-family: SimHei">{{ bookData.authorInfo.name }}</span>
                    <a style="font-size: 13px;font-family: SimHei;float: right;margin-left: 15px" @click="followUser">关注</a>
                    <a style="font-size: 13px;font-family: SimHei;float: right" @click="bookLike">点赞</a>
                  </div>
                </span>
              </span>
              </a-card>
            </div>
            <br/>
          </div>
          <div style="box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;margin-top: 15px">
            <div style="font-size: 16px;font-family: SimHei;padding: 15px;font-weight: 600">
              章节列表
            </div>
            <div style="padding: 15px">
              <a-list item-layout="horizontal" :data-source="bookDetailList">
                <a-list-item slot="renderItem" slot-scope="item, index">
                  <a-list-item-meta>
                    <a slot="title" @click="detailOpen(item)">{{ item.name }} <a-icon type="lock" theme="twoTone" v-if="item.checkFlag == 1"/></a>
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </div>
          </div>
          <div style="box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;margin-top: 15px">
            <div style="font-size: 16px;font-family: SimHei;padding: 15px;font-weight: 600">
              作品评论
              <a style="font-size: 13px;margin-left: 10px;font-weight: 500" @click="visible1 = true">添加评论</a>
            </div>
            <div v-for="(item, index) in evaluateList" :key="index">
              <a-comment style="padding: 15px">
                <a slot="author"><a-icon type="crown" theme="twoTone" /> {{ item.userName }}</a>
                <a-avatar
                  slot="avatar"
                  :src="'http://127.0.0.1:9527/imagesWeb/' + item.userImages.split(',')[0]"
                  alt="Han Solo"
                />
                <p slot="content">
                  {{ item.content }}
                </p>
                <a-tooltip slot="datetime" :title="moment(item.createDate).format('YYYY-MM-DD HH:mm:ss')">
                  <span>{{ moment(item.createDate).fromNow() }}</span>
                </a-tooltip>
              </a-comment>
            </div>
            <div v-if="evaluateList.length === 0">
              <div style="text-align: center;padding: 20px">暂无评论</div>
            </div>
            <div style="margin-bottom: 150px"></div>
          </div>
        </a-col>
        <a-col :span="18">
          <div style="padding: 15px" v-if="bookDetail != null">
            <div>
              <span style="font-size: 16px;font-family: SimHei;font-weight: 600">{{ bookDetail.name }}</span>
              <span style="margin-left: 30px;font-family: SimHei;font-size: 13px">字数：{{ bookDetail.words }}</span>
            </div>
            <div style="margin-top: 50px;font-family: SimHei">
              <div v-html="bookDetail.content" :style="styleClass">
              </div>
            </div>
          </div>
          <div style="margin-bottom: 100px"></div>
        </a-col>
      </a-row>
    </a-form>
    <div class="drawer-bootom-button">
      <a-button key="submit" type="primary" @click="chooseStyle" style="margin-right: 10px">
        选择模板
      </a-button>
      <a-popconfirm title="确定关闭？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">关闭</a-button>
      </a-popconfirm>
    </div>
    <a-modal
      title="选择格式模板"
      :visible="visible"
      @ok="checkStyle"
      @cancel="handleStyleCancel"
    >
      <a-form :form="form" layout="vertical">
        <a-row :gutter="20">
          <a-col :span="24">
            <a-radio-group button-style="solid" v-model="styleClassTemp">
              <a-radio-button :value="item" v-for="(item, index) in styleList" :key="index">
                {{ item.name }}
              </a-radio-button>
            </a-radio-group>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
    <a-modal
      title="添加数据评论"
      :visible="visible1"
      @ok="checkStyle1"
      @cancel="handleStyleCancel1"
    >
      <a-form :form="form" layout="vertical">
        <a-row :gutter="20">
          <a-col :span="12">
            <a-form-item label='评价分数' v-bind="formItemLayout">
              <a-rate v-decorator="[
            'score',
            { rules: [{ required: true, message: '请输入评价分数!' }] }
            ]" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label='评价内容' v-bind="formItemLayout">
              <a-textarea :rows="6" v-decorator="[
            'content',
             { rules: [{ required: true, message: '请输入评价内容!' }] }
            ]"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </a-drawer>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
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
  name: 'bookAdd',
  components: { Editor, Toolbar },
  props: {
    bookAddVisiable: {
      default: false
    },
    bookData: {
      default: null
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bookAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      styleClass: {
        backgroundColor: '',
        color: ''
      },
      styleClassTemp: '{}',
      visible: false,
      visible1: false,
      moment,
      evaluateList: [],
      bookDetailList: [],
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      bookList: [],
      styleList: [],
      previewVisible: false,
      previewImage: '',
      editor: null,
      bookDetail: null,
      memberInfo: null,
      html: '',
      title: '',
      toolbarConfig: { },
      editorConfig: { placeholder: '请输入内容...' },
      mode: 'default'
    }
  },
  watch: {
    bookAddVisiable: function (value) {
      console.log(value)
      if (value) {
        this.selectEvaluateBookList(this.bookData.id)
        this.selectBookDetailList(this.bookData.id)
      } else {
        this.bookDetail = null
      }
    }
  },
  mounted () {
    this.selectStyleList()
    this.selectMemberByUserId()
  },
  methods: {
    chooseStyle () {
      this.visible = true
    },
    handleStyleCancel () {
      this.visible = false
    },
    handleStyleCancel1 () {
      this.visible1 = false
    },
    checkStyle () {
      this.styleClass.backgroundColor = this.styleClassTemp.backClass
      this.styleClass.color = this.styleClassTemp.styleClass
      this.visible = false
    },
    checkStyle1 () {
      this.form.validateFields((err, values) => {
        values.bookId = this.bookData.id
        values.userId = this.currentUser.userId
        if (!err) {
          this.$post('/cos/evaluate-info', {
            ...values
          }).then((r) => {
            this.visible1 = false
            this.$message.success('评论成功')
            this.selectEvaluateBookList(this.bookData.id)
          })
        }
      })
    },
    followUser () {
      let data = {userId: this.currentUser.userId, authorId: this.bookData.authorId}
      this.$post('/cos/follow-info', data).then((r) => {
        this.$message.success('关注成功')
      })
    },
    bookLike () {
      let data = {userId: this.currentUser.userId, authorId: this.bookData.authorId, bookId: this.bookData.id}
      this.$post('/cos/book-like-info', data).then((r) => {
        this.$message.success('点赞成功')
      })
    },
    detailOpen (item) {
      if (this.memberInfo == null && item.checkFlag.toString() === '1') {
        this.$message.warn('该章节需要会员才能查看，请购买会员！')
        return false
      }
      this.$get('/cos/book-detail-info/views/edit', {detailId: item.id}).then((r) => {
      })
      this.bookDetail = item
    },
    selectMemberByUserId () {
      this.$get(`/cos/member-order-info/member/${this.currentUser.userId}`).then((r) => {
        this.memberInfo = r.data.member
      })
    },
    selectStyleList () {
      this.$get('/cos/style-info/list').then((r) => {
        this.styleList = r.data.data
      })
    },
    selectEvaluateBookList (bookId) {
      this.$get('/cos/evaluate-info/selectListByBookId/list', {bookId}).then((r) => {
        this.evaluateList = r.data.data
      })
    },
    selectBookDetailList (bookId) {
      this.$get('/cos/book-detail-info/list/book', {bookId}).then((r) => {
        this.bookDetailList = r.data.data
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
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        values.content = this.editor.getHtml()
        if (!err) {
          this.loading = true
          this.$post('/cos/book-detail-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
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
  >>> .ant-drawer-body {
    padding: 0 !important;
  }
  >>> .ant-comment-content-detail p {
    white-space: pre-line;
  }
</style>
