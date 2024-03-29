$(function(){
    countDown();
});

function countDown(){
    var remainSeconds = $("#remainSeconds").val();
    var timeout;
    if(remainSeconds > 0){//秒杀还没开始，倒计时
        $("#buyButton").attr("disabled", true);
        timeout = setTimeout(function(){
            $("#countDown").text(remainSeconds - 1);
            $("#remainSeconds").val(remainSeconds - 1);
            countDown();
        },1000);
    }else if(remainSeconds == 0){//秒杀进行中
        $("#buyButton").attr("disabled", false);
        if(timeout){
            clearTimeout(timeout);
        }
        $("#miaoshaTip").html("秒杀进行中");
    }else{//秒杀已经结束
        $("#buyButton").attr("disabled", true);
        $("#miaoshaTip").html("秒杀已经结束");
    }
}