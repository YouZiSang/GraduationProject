import useUserInfoStore from "@/stores/userInfo.js";

export function getUserInfo() {
    const userInfoStore = useUserInfoStore();
    return userInfoStore.info
}

export function getId(){
    const { uid, cid } = getUserInfo();
    return uid || cid;
}

export function getAvatar(){
    const { avatarUrl } = getUserInfo();
    return avatarUrl;
}

export function getName(){
    const { name, nickname } = getUserInfo();
    return name || nickname;

}

export function getRole(){
    const {role} = getUserInfo();
    return role;
}