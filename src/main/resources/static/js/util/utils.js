/**
 * web socket 示例代码
 * @param url   web socket 连接路径
 * @param data  传递到后台的JSON数据
 * @param fct   处理推送数据的函数
 */
var websocket = function (url, data, fct) {
        /*web socket 初始化*/
        var FEDBAKWS = function () {
            this.socket;

            /*web socket 建立连接*/
            this.onOpen = function (e) {
                if (typeof data != null) {
                    this.socket.send(JSON.stringify(data));
                }
                console.log(e);
            };

            /*web socket 消息接收*/
            this.onmessage = function (e) {
                this.handleEvent(e);
            };

            /*web socket 错误*/
            this.onerror = function (e) {
                console.log('Error Web Socket');
                this.reConnect();
            };

            /*web socket 连接中断*/
            this.onclose = function (e) {
                console.log('Close Web Socket')
                this.reConnect();
            };
            this.initWS();
        };

        /*web socket 连接*/
        FEDBAKWS.prototype.initWS = function () {
            window.WebSocket = window.WebSocket || window.MozWebSocket;

            if (!window.WebSocket) {    // 检测浏览器支持
                console.error('错误: 浏览器不支持 Web Socket');
                return;
            }

            var that = this;
            this.socket = new WebSocket(url);
            this.socket.onopen = function (e) {
                that.onOpen(e);
            };
            this.socket.onmessage = function (e) {
                that.onmessage(e);
            };
            this.socket.onclose = function (e) {
                that.onclose(e);
            };
            this.socket.onerror = function (e) {
                that.onerror(e);
            };

            return this;
        };

        /*web socket 重连*/
        FEDBAKWS.prototype.reConnect = function () {
            var _that = this;
            if (_that.socket.readyState != 3) return;   // 0 正在建立连接 1 已建立连接 2 正在关闭连接 3 已关闭连接

            setTimeout(function () {            //定时重连
                if (_that.socket.readyState == 3) {
                    _that.initWS();
                    _that.isReconnect = true;
                }
            }, 5000);
        };

        FEDBAKWS.prototype.handleEvent = function (message) {
            fct(message);
        };

        FEDBAKWS.prototype.send = function (message) {
            this.socket.send(message);
        };
    return new FEDBAKWS(url);
};


/**
 * 时间类型转换
 * @param time
 * @param format
 * @returns {void | string}
 */
var format = function (time, format) {
    var t = new Date(time);
    var tf = function (i) {
        return (i < 10 ? '0' : '') + i
    };
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
        switch (a) {
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    })
};