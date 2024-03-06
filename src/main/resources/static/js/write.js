console.log("안녕")
function dowrite(){
    //1.html 입력값 호출
    let id = document.querySelector("#id").value
    let pw = document.querySelector("#pw").value
    let title = document.querySelector("#title").value
    let content = document.querySelector("#content").value
    console.log([id,pw])
    //2.객체화
    let info = {
        id : id, pw:pw , content : content , title : title

    }
    console.log(info)
    //3.객체를 배열에 저장 ---> 스프링 컨트롤러 서버와 연동
    //4.결과
    $.ajax({
            url : '/check',
            method : 'POST',
            data :{content:content},
            success : (result) => {
            if(result.length != 0){
                console.log(result)
                let filter = "[";
                for(let i=0; i<result.length; i++){
                    filter += result[i]+",";
                }
                filter += "]"
                alert(`${filter}는 사용할수 없는 단어입니다`)
            }
            else{
                $.ajax({
                        url : '/write',
                        method : 'POST',
                        data :info,
                        success : (result) => {
                        if(result){
                         alert("작성성공");
                        }
                     }
                })
            }
         }
    })
}

function dorecommend(){
    let contentall = document.querySelector("#content").value
    let contentlist = contentall.split(" ")
    let content = contentlist[contentlist.length-1]
    console.log(content);
    if(content.length >= 1){   
     $.ajax({
            url : '/recommend',
            method : 'POST',
            data :{content:content},
            success : (result) => {
            if(result.length != 0){
                document.querySelector("#auto").innerHTML = result;
                }
            }
        })
    }
    else{
        document.querySelector("#auto").innerHTML = "추천단어";
    }
}