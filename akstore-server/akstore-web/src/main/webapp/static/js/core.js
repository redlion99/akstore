if(typeof console == 'undefined'){
    console={log:function(){}}
}

var qs = (function(a) {
    if (a == "") return {};
    var b = {};
    for (var i = 0; i < a.length; ++i)
    {
        var p=a[i].split('=', 2);
        if (p.length == 1)
            b[p[0]] = "";
        else
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
    }
    return b;
})(window.location.search.substr(1).split('&'));

(function($,qs){

    $.fn.bigwin = function(options, param){
        if (typeof options == 'string'){
            var method = Array.prototype.shift.call(arguments);
            return $.fn.bigwin.methods[method].apply(this, arguments);
        }
    };

    $.fn.bigwin.methods = {
        loadView: function(view,dataUrl,fn){
            var self=this;
            if (typeof dataUrl == 'function'){
                fn=dataUrl;
                dataUrl=null;
            }
            self.load('views/'+view+'.html',function(){
                $.parser.parse(self);
                if(dataUrl){
                    $.get(dataUrl,function(result){
                        //$(self,'.easyui-datagrid').datagrid('loadData',result);

                        if(fn){
                            fn.call(self,result);
                        }
                    });
                }else if(fn){
                    fn.call(self);
                }
            });
            return this;
        },
        transition:function(){
            var self=this;
            $.transition(self.attr('title'),self.attr('content'));
        }
    }

    var auth_header='';
    if(qs['u']){
        auth_header="Basic " + btoa(qs['u'] + ":" + qs['t']);
    }

    function do_http(method,url,data,success){
        var options={
                  type: method,
                  url: url,
                  headers: {
                    "Authorization": auth_header
                  },
                  data: data,
                  success: success,
                  dataType:'json'
                };
                $.ajax(options);
    }

    $.sget=function(url,data,success){
        do_http("GET",url,data,success);
    }

    $.spost=function(url,data,success){
        do_http("POST",url,data,success);
    }

    $.sdelete=function(url,data,success){
            do_http("DELETE",url,data,success);
        }

    $.navigate=function(layout){
        $('.content').bigwin('loadView','layouts/'+layout);
    }

    $.transition=function(title,content,button1,button2){
        $('.header .panel-title').html(title);
        $('.content').bigwin('loadView',content);
        if(button1)
            $('.header .left').append(button1);
        if(button2)
            $('.header .left').append(button2);
    }

    $.geo=function(){
        
        return{
            coords:null,
            get:function(fn){
                var self=this;
                if (navigator.geolocation) {
                    console.log(self);
                    navigator.geolocation.getCurrentPosition(function(position){
                        console.log(position);
                        self.coords={lat:position.coords.latitude,lng:position.coords.longitude}
                        if(fn)
                            fn.call(self,self.coords);
                    }, function(err){
                        console.log(err);
                        if(fn)
                            fn.call(self,self.coords);
                    },{
                        enableHighAcuracy: false,
                        timeout: 15000,
                        maximumAge: 3000
                    });
                }
                return self.coords;
            }
        }
    }();

    


})(jQuery,qs);




var geolocation = function(address,cb){
    var MSearch;
    AMap.service(["AMap.PlaceSearch"], function() {
        MSearch = new AMap.PlaceSearch({ //构造地点查询类
            pageSize:10,
            pageIndex:1,
            city:"021" //城市
        });
        //关键字查询
        MSearch.search(address, function(status, result){
        	if(status === 'complete' && result.info === 'OK'){
        		cb(result);
        	}
        });
    });

}

var flat=function(flated,name){
    var obj=this;
    if(typeof obj =='object'&&obj!=window){
        for(var k in obj){
            if(obj[k]&&!$.isArray(obj[k])&&typeof obj[k] == 'object')
                flat.call(obj[k],flated,k);
            else if($.isArray(obj[k])&&obj[k].length>0){
                flated['children']=flatArray(obj[k]);
            }else
                flated[name+'_'+k]=obj[k];
        }
    }
}

var flatObject=function(obj){
    var flated={};
    flat.call(obj,flated,'');
    return flated;
}

var flatArray=function(arr){
    for(var i=0;i<arr.length;i++){
        var flated={};
        flat.call(arr[i],flated,'');
        arr[i]=flated;
    }
    return arr;
}
var file_size=null;

function uploadAttachment(url,fn){
    $('#uploadDlg').dialog('open');
    $('#fileuploadForm').form({url:url,
            onSubmit: function(){
                if(file_size){
                    $('.',this)
                }
                var bl = $(this).form("validate");
                return bl;
            },
            success:function(data){ 
                data=JSON.parse(data);
                fn(data);
                $('#uploadDlg').dialog('close');
            }
        });
}
