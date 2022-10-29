package sillybaka.springframework.aop;

/**
 * Description：
 * <p>Date: 2022/10/29
 * <p>Time: 17:39
 *
 * @Author SillyBaka
 **/
public class NiubiService {

    public void niubi(){
        System.out.println("牛逼啊Cglib");
    }

    public String six(String str){
        System.out.println(str);

        return str;
    }
}
