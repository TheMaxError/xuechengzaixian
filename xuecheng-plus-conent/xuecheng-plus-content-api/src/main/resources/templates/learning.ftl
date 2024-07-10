<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="http://localhost/static/img/asset-favicon.ico">
    <title>学成在线-在线学习</title>

    <link rel="stylesheet" href="http://localhost/static/plugins/rainbow.css" />
    <link rel="stylesheet" href="http://localhost/static/plugins/normalize-css/normalize.css" />
    <link rel="stylesheet" href="http://localhost/static/plugins/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="http://localhost/css/page-learing-course-videoes.css" />
    <link rel="stylesheet" href="http://localhost/static/css/el/index.css" />
    <script src="http://localhost/static/js/vue/vue.min.js"></script>
    <script src="http://localhost/static/js/axios/axios.min.js"></script>
    <script src="http://localhost/static/js/util.js"></script>
    <script src="http://localhost/static/js/public.js"></script>
    <script src="http://localhost/static/css/el/index.js"></script>
    <script src="http://localhost/static/js/learning.js"></script>
</head>

<body >
<!-- 页面头部 -->
<div class="learing-course">
    <div >
        <div class="course-cont">
            <div class="course-cont-top-video" style="position: relative;">
                <div class="video-box">
                    <div class="top text-center" >
                        <div id="courseNameText"></div>
                        <!--<i class="glyphicon glyphicon-book pull-right"></i>-->
                        <!--<div class="resources">-->
                        <!--<div class="resou-box">-->
                        <!--<div class="resources-tit"><i class="glyphicon glyphicon-folder-close"></i>课程资源</div>-->
                        <!--<div class="tit">课程材料</div>-->
                        <!--<div><i class="glyphicon glyphicon-floppy-save"></i>软件安装包</div>-->
                        <!--<div class="tit">其他</div>-->
                        <!--<div><i class="glyphicon glyphicon-download-alt"></i>字幕下载</div>-->
                        <!--<div><i class="glyphicon glyphicon-flag"></i>报告问题</div>-->
                        <!--</div>-->
                        <!--</div>-->
                    </div>
                    <div class="video text-center" style="padding-right: 55px;">
                        <div class="video-mine">
                            <div class="cls-text"><a class="media" href=""></a></div>
                            <div class="cls-video">
                                <div id="vdplay"></div>
                                <div class="vdControl">
                                    <!--<div class="play" onclick="vdPlay()">播放</div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="course-nav">
                        <div class="nav-stacked text-center">
                            <li class="nav"><a href="javascript:;" class="glyphicon glyphicon-list-alt"><span>目录</span></a></li>
                            <li class="lab-note"><a href="javascript:;" class="glyphicon glyphicon-user"><span>笔记</span></a></li>
                            <li class="lab-ask"><a href="javascript:;" class="glyphicon glyphicon-cog"><span>问答</span></a></li>
                            <li class="lab-com"><a href="javascript:;" class="glyphicon glyphicon-duplicate"><span>评论</span></a></li>
                        </div>
                    </div>
                    <div class="note">
                        <div class="note-box">
                            <div class="note-lab"><span class="active">我的笔记</span><span>精选笔记</span></div>
                            <div class="note-item-cont">
                                <div class="my-note">
                                    <div class="textarea-box">
                                        <textarea name="my-note" id=""></textarea>
                                        <div class="info"><span class="time"><i class="i-play"></i>23`22` </span><span class="not-but">提交</span></div>
                                    </div>
                                    <ul class="my-item-box">
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                    </ul>
                                    <a href="#" class="more">查看更多</a>
                                </div>
                                <div class="sel-note">
                                    <ul class="my-item-box">
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="time"><i class="i-play"></i>23`22` </span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                    </ul>
                                    <a href="#" class="more">查看更多</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="ask">
                        <div class="ask-box">
                            <div class="note-lab"><span class="active">我的问答</span><span>精选问答</span></div>
                            <div class="note-item-cont">
                                <div class="my-note">
                                    <div class="textarea-box">
                                        <input type="text" class="ask-title" value="" placeholder="请输入问题标题">
                                        <textarea name="my-note" id="" placeholder="请输入问题描述"></textarea>
                                        <div class="info"><span class="pic" onclick="screenshot()">截图 </span><span class="not-but">保存</span></div>
                                    </div>
                                    <ul class="my-item-box">
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-29</span>
                                                <div class="oper-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></div>
                                            </div>
                                        </li>
                                    </ul>
                                    <a href="#" class="more">查看更多</a>
                                </div>
                                <div class="sel-note">
                                    <ul class="my-item-box">
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div><span><img src="http://localhost/static/img/asset-vid.png" alt=""></span><span class="name">王老师</span><span class="our-answer">我来回答</span></div>
                                            <div class="tit">这个是我想问的问题</div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>4 小时前</span>
                                                <div class="oper-box"><span>回答 2</span><span>浏览 4</span></div>
                                            </div>
                                        </li>
                                    </ul>
                                    <a href="#" class="more">查看更多</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="com">
                        <div class="com-box">
                            <!--<div class="note-lab"><span class="active">我的问答</span><span>精选问答</span></div>-->
                            <div class="note-item-cont">
                                <div class="my-note">
                                    <div class="evaluate">
                                        <div class="eva-top">
                                            <div class="tit">课程评分 </div>
                                            <div class="star">
                                                <div class="score"><i>5</i></div>
                                            </div><span class="star-score"> <i>5</i> 分</span></div>
                                        <div class="eva-cont">
                                            <div class="tit">学员评语 </div>
                                            <div class="text-box">
                                                <textarea class="form-control" rows="5" placeholder="扯淡、吐槽、表扬、鼓励......想说啥说啥！"></textarea>
                                                <div class="text-right"><span>发表评论</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--<div class="textarea-box">-->
                                    <!--<input type="text" class="ask-title" value="" placeholder="请输入问题标题">-->
                                    <!--<textarea name="my-note" id=""  placeholder="请输入问题描述"></textarea>-->
                                    <!--<div class="info"><span class="pic" onclick="screenshot()">截图 </span><span class="not-but">保存</span></div>-->
                                    <!--</div>-->
                                    <ul class="my-item-box">
                                        <li>
                                            <div>
                                                <span><img src="http://localhost/static/img/asset-vid.png" alt=""></span>
                                                <span class="name">王老师</span>
                                                <div class="item-rit">
                                                    <div class="star-show">
                                                        <div class="score"><i>4</i></div>
                                                    </div>
                                                    <div class="showSt">评分 <span>5星</span></div>
                                                </div>
                                            </div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-23</span>
                                                <div class="oper-box"></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <span><img src="http://localhost/static/img/asset-vid.png" alt=""></span>
                                                <span class="name">王老师</span>
                                                <div class="item-rit">
                                                    <div class="star-show">
                                                        <div class="score"><i>4</i></div>
                                                    </div>
                                                    <div class="showSt">评分 <span>5星</span></div>
                                                </div>
                                            </div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-23</span>
                                                <div class="oper-box"></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <span><img src="http://localhost/static/img/asset-vid.png" alt=""></span>
                                                <span class="name">王老师</span>
                                                <div class="item-rit">
                                                    <div class="star-show">
                                                        <div class="score"><i>4</i></div>
                                                    </div>
                                                    <div class="showSt">评分 <span>5星</span></div>
                                                </div>
                                            </div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-23</span>
                                                <div class="oper-box"></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <span><img src="http://localhost/static/img/asset-vid.png" alt=""></span>
                                                <span class="name">王老师</span>
                                                <div class="item-rit">
                                                    <div class="star-show">
                                                        <div class="score"><i>4</i></div>
                                                    </div>
                                                    <div class="showSt">评分 <span>5星</span></div>
                                                </div>
                                            </div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-23</span>
                                                <div class="oper-box"></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <span><img src="http://localhost/static/img/asset-vid.png" alt=""></span>
                                                <span class="name">王老师</span>
                                                <div class="item-rit">
                                                    <div class="star-show">
                                                        <div class="score"><i>4</i></div>
                                                    </div>
                                                    <div class="showSt">评分 <span>5星</span></div>
                                                </div>
                                            </div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-23</span>
                                                <div class="oper-box"></div>
                                            </div>
                                        </li>
                                        <li>
                                            <div>
                                                <span><img src="http://localhost/static/img/asset-vid.png" alt=""></span>
                                                <span class="name">王老师</span>
                                                <div class="item-rit">
                                                    <div class="star-show">
                                                        <div class="score"><i>4</i></div>
                                                    </div>
                                                    <div class="showSt">评分 <span>5星</span></div>
                                                </div>
                                            </div>
                                            <div class="cont">
                                                课程内容 消息回复接口（图文,语音消息的自动回复） 素材管理接口（图片上传） 自定义菜单接口（菜单的创建，查询，删除）
                                            </div>
                                            <div class="operation"><span>2017-2-23</span>
                                                <div class="oper-box"></div>
                                            </div>
                                        </li>
                                    </ul>
                                    <a href="#" class="more">查看更多</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="navCont" id="teachplanApp">
                        <div class="course-weeklist">
                            <!--
                             <div class="nav-stacked">
                                 <div class="tit nav-justified text-center"><i class="pull-left glyphicon glyphicon-th-list"></i>第一周软件安装 <i class="pull-right">1/4</i></div>
                                 <li><i class="glyphicon glyphicon-check"></i><a href=javascript:vdPlay()>分级政策细分</a></li>
                                 <li><i class="glyphicon glyphicon-unchecked"></i>为什么分为A、B、C部分</li>
                                 <li><i class="glyphicon glyphicon-unchecked"></i>软安装介绍</li>
                                 <li><i class="glyphicon glyphicon-unchecked"></i>Emacs安装 </li>
                             </div>
                             -->
                            <div class="nav-stacked" v-for="(teachplan_first, index) in teachplans">
                                <div class="tit nav-justified text-center"><i class="pull-left glyphicon glyphicon-th-list"></i>{{teachplan_first.pname}} <i class="pull-right">1/4</i></div>
                                <li  v-if="teachplan_first.teachPlanTreeNodes!=null" v-for="(teachplan_second, index) in teachplan_first.teachPlanTreeNodes"><i class="glyphicon glyphicon-unchecked"></i>
                                    <a href="#" @click="vdPlay(teachplan_second.teachplanMedia)">{{teachplan_second.pname}}</a>
                                </li>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


        </div>
    </div>
    <!-- 页面底部 -->
    <!-- 页面 css js -->

    <script type="text/javascript" src="http://localhost/static/plugins/jquery/dist/jquery.js"></script>
    <script type="text/javascript" src="http://localhost/static/plugins/js-pdf/jquery.media.js"></script>
    <script type="text/javascript" charset="utf-8" src="http://localhost/plugins/ckplayer/ckplayer.js"></script>
    <script type="text/javascript">
        $(function() {
            $('.active-box span').click(function() {
                $(this).css({
                    'color': '#00a4ff'
                })
                if ($(this).find('i').hasClass('i-laud')) {
                    $(this).find('.i-laud').css('background-position', '-80px -19px')
                } else if ($(this).find('i').hasClass('i-coll')) {
                    $(this).find('.i-coll').css('background-position', '1px -75px')
                }
            })
            $('.learing-box .item-list').mouseover(function(e) {
                $(this).css({
                    'height': '140px'
                }).addClass('hov').siblings().css({
                    'height': '50px'
                })
                $(this).siblings().removeClass('hov')
            })
            $('.learing-box .item-box').mouseout(function() {
                $(this).find('.item-list:first').css({
                    'height': '140px'
                }).addClass('hov')
                $(this).find('.item-list:first').siblings().css({
                    'height': '50px'
                }).removeClass('hov')
            })
        })


        $(function() {
            $('.learing-box .item-list').mouseover(function(e) {
                $(this).css({
                    'height': '140px'
                }).addClass('hov').siblings().css({
                    'height': '50px'
                })
                $(this).siblings().removeClass('hov')
            })
            $('.learing-box .item-box').mouseout(function() {
                $(this).find('.item-list:first').css({
                    'height': '140px'
                }).addClass('hov')
                $(this).find('.item-list:first').siblings().css({
                    'height': '50px'
                }).removeClass('hov')
            })
        })
    </script>
    <script type="text/javascript" src="/plugins/rainbow-custom.min.js"></script>
    <script type="text/javascript">
        $(function() {
            //代码点击显示
            $(".item .item-left").click(function() {
                var pre = $(this).parent();
                if (!pre.find('pre').hasClass('code-pop')) {
                    pre.find('pre').addClass('code-pop');
                    pre.find('.mask,pre').css('display', 'block')
                } else {
                    pre.find('pre').removeClass('code-pop');
                    pre.find('.mask,pre').css('display', 'none')
                }
            });
            //代码切换
            $('.content-title p a').click(function() {
                $(this).addClass('all').siblings().removeClass('all');
            })

            $('.learing-box .item-list').mouseover(function(e) {
                $(this).css({
                    'height': '140px'
                }).addClass('hov').siblings().css({
                    'height': '50px'
                })
                $(this).siblings().removeClass('hov')
            })
            $('.learing-box .item-box').mouseout(function() {
                $(this).find('.item-list:first').css({
                    'height': '140px'
                }).addClass('hov')
                $(this).find('.item-list:first').siblings().css({
                    'height': '50px'
                }).removeClass('hov')
            })
        })


        $(function() {
            //评分
            $('.star .score').map(function(n, i) {
                var x = Number($(this).find('i').text());
                var w = 109 * (1 - x / 5);
                $(this).css('width', w + 'px');
            })
            //评论打分
            $('.evaluate .star').mousemove(function(e) {
                var startX = $(this).offset().left;
                var movX = e.clientX - startX + 0.5;
                var w = 145 * (1 - movX / 145);
                $(this).find('.score').css('width', w + 'px');
                $('.star-score i').text((movX / 145 * 5).toFixed(1))
            })
            //星级评分
            $('.grade').map(function(n, i) {
                var pret = $(this).find('.percent-num i').text();
                var wt = $(this).find('.grade-percent').width();
                $(this).find('.grade-percent span').css('width', wt * pret / 100);
            })



        })


        //文本学习 PDF
        $(function() {
            $('.cls-text a.media').media({
                width: '100%',
                height: '100%'
            });
        });

        // 在线发送！
        $('.online-submit').click(function() {
            var cont = $('.online-cont').val()
            $('.chat-box').append('<li class="rt"><span><img src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517221250282&di=33dec77d7966a70bb1edef24cc93692d&imgtype=jpg&src=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D593340121%2C2144442477%26fm%3D214%26gp%3D0.jpg" width="30" alt=""></span>' + cont + '</li>')
            $('.online-cont').val('')
        })

        // tab切换
        $('.subtitle-cont .tit span').click(function() {
            swiTab($(this).attr('id'))
        })
        // tab切换  及  问题 代码点击函数
        function swiTab(obj) {
            console.log(obj)
            if (obj) {
                $('#' + obj).addClass('active').siblings().removeClass('active');
                var clasNod = '.' + obj
            } else {
                $(this).addClass('active').siblings().removeClass('active');
                var clasNod = '.' + $(this)[0].id
            }
            window.location = '#txt'
            $('.subtitle-cont').find(clasNod).show().siblings().hide()
        }

        //截图  //通过ajax提交 当前播放时间给后台 后台用软件截图 返回图片路径
        function screenshot() {
            var dataes = [times]
            //    $.ajax({
            //      url: "test.html",
            //      data: dataes,
            //      success: function(){
            //        $(this).addClass("done");
            //      }
            //    });
        }
        // 关闭
        $('.clone').click(function() {
            $('.popup').hide()
        })
        // 回复
        $('.hf-onck').click(function() {
            $('.hf-answer').show()
        })
        // 回复取消
        $('.but-item .can').click(function() {
            $('.hf-answer').hide()
        })
    </script>
    <script type="text/javascript" src="http://localhost/static/plugins/bootstrap/dist/js/bootstrap.js"></script>
    <script>
        $(function() {
            //var vidHit = $('html').height() - 70;
            var vidHit = $('html').height() ;
            var vidCenHit = (vidHit - $('.video-play').height()) / 2;
            var vdwt = $('.video').width();
            var wt = 300;

            $('.course-cont-top-video,.video').css('height', vidHit);
            $('.video').css('height', vidHit - 50);
            $('.note-cont').css('height', vidHit - $('.note-box .note').height() - 65);

            $(window).resize(function() {
                $('.course-cont-top-video,.video').css('height', vidHit);
                $('.video').css('height', vidHit - 50);
                $('.nodte-cont').css('height', vidHit - $('.note-box .note').height() - 65);
            });
            //还原初始状态-控制器
            function setStart(obj) {
                if (obj === 'ck') { //左导航
                    setnt()
                    setak()
                    setcm()
                    setnav()
                } else if (obj === 'nt') { // 笔记
                    setak()
                    setck()
                    setcm()
                    setnav()
                } else if (obj === 'ak') { // 问答
                    setck()
                    setnt()
                    setcm()
                    setnav()
                } else if (obj === 'cm') { // 评论
                    setck()
                    setnt()
                    setak()
                    setnav()
                } else if (obj === 'nv') { // 目录
                    setck()
                    setnt()
                    setcm()
                    setak()
                }
            }

            function setck() {
                if ($('.video-box .glyphicon-align-justify').hasClass('ck')) {
                    $('.video-box .glyphicon-align-justify').removeClass('ck');
                    $('.video-box').animate({
                        width: '100%'
                    }, 500)
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 0)
                    $('.course-weeklist').animate({
                        left: -wt
                    }, 500)
                }
            }

            function setnt() {
                if ($('.course-nav .lab-note').hasClass('nt')) {
                    $('.course-nav .lab-note').removeClass('nt');
                    $('.note').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 0)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            }
            //
            function setnav() {
                if ($('.course-nav .nav').hasClass('nv')) {
                    $('.course-nav .nav').removeClass('nv');
                    $('.navCont').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 0)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            }
            //
            function setak() {
                if ($('.course-nav .lab-ask').hasClass('ak')) {
                    $('.course-nav .lab-ask').removeClass('ak');
                    $('.ask').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 0)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            }
            // 讲义
            function setcm() {
                if ($('.course-nav .lab-com').hasClass('cm')) {
                    $('.course-nav .lab-com').removeClass('cm');
                    $('.com').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 0)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            }
            //
            $('.note-box .problem').click(function() {
                alert('wenda')
                if (!$(this).hasClass('ck')) {
                    $(this).addClass('ck');
                    $(this).find('p').css('display', 'block')
                    $('.note-cont').css('height', vidHit - $('.note-box .note').height() - 65);
                } else {
                    $(this).removeClass('ck');
                    $(this).find('p').css('display', 'none')
                    $('.note-cont').css('height', vidHit - $('.note-box .note').height() - 65);
                }
            });
            //
            var reht = $(".video-box .resou-box").height();
            $(".video-box .pull-right").click(function() {
                if (!$(this).hasClass('ck')) {
                    $(this).addClass('ck');
                    $(".video-box .resources").animate({
                        height: reht
                    }, 500)
                } else {
                    $(this).removeClass('ck');
                    $(".video-box .resources").animate({
                        height: 0
                    }, 500)
                }
            })
            //笔记切换
            $('.note-box .note-lab span, .ask-box .note-lab span').click(function() {
                $(this).addClass('active').siblings().removeClass('active')
                if ($(this).index() == 1) {
                    $('.note-item-cont').animate({
                        'left': -400
                    }, 500)
                } else {
                    $('.note-item-cont').animate({
                        'left': 0
                    }, 500)
                }
            })
            //笔记部分处理
            var myNtHt = $('.my-note .textarea-box').height();
            $('.my-note .my-item-box').css('height', $('.note-box').height() - myNtHt - 160 + "px");
            $('.sel-note .my-item-box').css('height', $('.note-box').height() - 160 + "px");
            $('.course-nav .lab-note').click(function() {
                window.location = '#'
                setStart('nt');
                if (!$(this).hasClass('nt')) {
                    $(this).addClass('nt');
                    $('.note').animate({
                        'width': "400px"
                    }, 500)
                    $('.course-nav').animate({
                        'right': "401px"
                    }, 500)
                    $('.video').animate({
                        'width': vdwt - 380 + 'px'
                    }, 500);
                } else {
                    $(this).removeClass('nt');
                    $('.note').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 500)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            })
            //目录
            $('.course-nav .nav').click(function() {
                window.location = '#'
                setStart('nv');
                if (!$(this).hasClass('nv')) {
                    $(this).addClass('nv');
                    $('.navCont').animate({
                        'width': "300px"
                    }, 500)
                    $('.course-nav').animate({
                        'right': "301px"
                    }, 500)
                    $('.video').animate({
                        'width': vdwt - 280 + 'px'
                    }, 500);
                } else {
                    $(this).removeClass('nv');
                    $('.navCont').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 500)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            })
            // 问答
            $('.course-nav .lab-ask').click(function() {
                window.location = '#'
                setStart('ak');
                if (!$(this).hasClass('ak')) {
                    $(this).addClass('ak');
                    $('.ask').animate({
                        'width': "400px"
                    }, 500)
                    $('.course-nav').animate({
                        'right': "401px"
                    }, 500)
                    $('.video').animate({
                        'width': vdwt - 380 + 'px'
                    }, 500);
                } else {
                    $(this).removeClass('ak');
                    $('.ask').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 500)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            })
            // 评论
            $('.course-nav .lab-com').click(function() {
                window.location = '#'
                setStart('cm');
                if (!$(this).hasClass('cm')) {
                    $(this).addClass('cm');
                    $('.com').animate({
                        'width': "400px"
                    }, 500)
                    $('.course-nav').animate({
                        'right': "401px"
                    }, 500)
                    $('.video').animate({
                        'width': vdwt - 380 + 'px'
                    }, 500);
                } else {
                    $(this).removeClass('cm');
                    $('.com').animate({
                        'width': "0"
                    }, 500);
                    $('.course-nav').animate({
                        'right': "0px"
                    }, 500)
                    $('.video').animate({
                        'width': '100%'
                    }, 500);
                }
            })
            //video播放器
            $('.video-mine .cls-text').click(function() {

            })

        })
        function timeHandler(time) {
            console.log('当前播放时间（秒）：' + time)
        }
        function durationHandler(duration){
            console.log('总时间（秒）：' + duration)
        }
        function loadedHandler(player) {
            var fun = new function(){
                player.addListener('time', timeHandler); //监听播放时间
                player.addListener('duration', durationHandler); //监听播放时间
            }
            return fun;
        }

        var learningVm= new Vue({
            el: "#teachplanApp",

            data: {
                videServer:'http://file.localhost',
                courseId:'',
                teachplanId:'',
                teachplans:[],
                videoObject : {
                    container: '#vdplay', //容器的ID或className
                    variable: 'player',//播放函数名称
                    poster:'/static/img/asset-video.png',//封面图片
                    //loaded: 'loadedHandler', //当播放器加载后执行的函数
                    //video:'http://file.localhost/video/1/f/1f229319d6fed3431d2f9d06193a433b/1f229319d6fed3431d2f9d06193a433b.mp4'
                    video:'http://file.localhost/video/0/3/0363a6759d297e9d0e5b4b86e8802aa3.mp4'
                    // video: [//视频地址列表形式
                    // 	['http://file.localhost/video/3/a/3a5a861d1c745d05166132c47b44f9e4/3a5a861d1c745d05166132c47b44f9e4.mp4', 'video/mp4', '中文标清', 0]
                    // ]
                },
                player : null,
                preview:false

            },
            methods: {
                getTeachplans() {

                },


                firstPlay(arr,teachplanId){
                    for(let i = 0; i < arr.length; i++) {
                        var teachplan_second= arr[i].teachPlanTreeNodes;
                        if(teachplan_second){
                            for(let j = 0; j < teachplan_second.length; j++) {
                                if(teachplan_second[j].teachplanMedia){
                                    if(teachplanId){
                                        //console.log("teachplanId="+teachplanId+"  "+teachplan_second[j].teachplanMedia.teachplanId)
                                        //播放指定章节
                                        if(teachplanId == teachplan_second[j].teachplanMedia.teachplanId){
                                            this.vdPlay(teachplan_second[j].teachplanMedia)
                                            return ;
                                        }
                                    }else{
                                        //播放第一个
                                        this.vdPlay(teachplan_second[j].teachplanMedia)
                                        return ;
                                    }

                                }
                            }
                        }
                    }

                },
                vdPlay(mediaFile) {

                    if(mediaFile && mediaFile.mediaId){
                        //获取视频
                        requestGetMeidaInfo(mediaFile.mediaId,mediaFile.teachplanId,this.courseId).then(res=>{
                            if(res&&res.result){
                                //console.log(res.result)
                                var newVideoObject = {
                                    container: '#vdplay', //容器的ID或className
                                    variable: 'player',//播放函数名称
                                    poster:'/static/img/asset-video.png',//封面图片
                                    loaded: loadedHandler(this.player), //当播放器加载后执行的函数
                                    video: this.videServer+res.result
                                }
                                // this.videoObject.video=this.videServer+res.result
                                // this.videoObject.loaded=this.loadedHandler
                                this.player.newVideo(newVideoObject);
                                this.player.videoPlay()
                            }else{
                                this.$message.error(res.msg)
                            }
                        }).catch(err => {
                            if(err && err.errMessage){
                                this.$message.error(err.errMessage)
                            }else{
                                this.$message.error("获取视频出错请稍后重试")
                            }

                        })

                    }else{
                        this.$message.error("播放失败，未关联视频")
                    }

                }

            },
            created() {
                if(String(window.location).indexOf("/course/preview/")>0){
                    //预览模式
                    this.preview=true;
                }
                this.player = new ckplayer(this.videoObject)
                var id = GetQueryString("id");
                var chapter = GetQueryString("chapter");
                if(!id){
                    this.$message.error('参数错误');
                    return ;
                }
                if(chapter){
                    this.teachplanId = chapter;
                }
                this.courseId = id;
                requestGetCourseInfo(this.courseId).then(res=>{
                    this.teachplans = res.teachplans;
                    console.log(res)
                    document.getElementById("courseNameText").innerHTML=res.courseBase.name;
                    //播放第一个视频
                    this.firstPlay(this.teachplans,this.teachplanId)
                }).catch(err => {
                    if(err && err.errMessage){
                        this.$message.error(err.errMessage)
                    }else{
                        this.$message.error("获取课程信息出错请稍后重试")
                    }

                });
            },
            mounted(){


            }
        })



    </script>
</body>
</html>