import axios from "axios";

const ADMIN_KEY = "81abfcdd0ae8ff4b65676cb6a27bcdba";

export const kakaoClient = axios.create({
    baseURL: "https://kapi.kakao.com",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Authorization: `KakaoAK ${ADMIN_KEY}`,
    },
    timeout: 10000,
});
