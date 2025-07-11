// src/pages/PaymentSuccess.jsx
import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import apiClient from "../util/apiInstance";

export default function PaymentSuccess() {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const pg_token = new URLSearchParams(location.search).get("pg_token");
        const tid = localStorage.getItem("kakao_tid");
        const username = localStorage.getItem("username"); // 추가
        const bookid = localStorage.getItem("bookid");     // 추가

        if (!pg_token) {
            alert("pg_token이 없습니다!");
            navigate("/payment/fail");
            return;
        }

        if (!tid) {
            alert("tid(kakao_tid)가 localStorage에 없습니다!");
            navigate("/payment/fail");
            return;
        }

        const approvePayment = async () => {
            try {
                const response = await apiClient.post("/api/kakao/approve", {
                    tid,
                    pg_token,
                    username,
                    bookid

                });

                alert("결제가 승인되었습니다!");
                navigate("/mypage/reservations");
            } catch (error) {
                console.error("결제 승인 실패:", error);
                alert("결제 승인 중 오류가 발생했습니다.");
                navigate("/payment/fail");
            }
        };

        approvePayment();
    }, [location.search, navigate]);

    return <p>결제를 승인 중입니다. 잠시만 기다려주세요...</p>;
}
