package weifen.com.common.db;

/**
 * 包装用户信息
 * Created by zhurencong on 2017/10/13.
 */
public class User {

    //用户id、头像链接、用户昵称、用户是否认证、用户评分、用户余额、个性签名、用户手机、订单个数
    private String userId,imageUrl,nickName,isProve,grade,money,signature,tel,transactionNums;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIsProve() {
        return isProve;
    }

    public void setIsProve(String isProve) {
        this.isProve = isProve;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTransactionNums() {
        return transactionNums;
    }

    public void setTransactionNums(String transactionNums) {
        this.transactionNums = transactionNums;
    }
}
