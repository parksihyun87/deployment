import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import apiClient from "../util/apiInstance";
import {kakaoClient} from "../util/kakaoInstatance";
import qs from "qs";

export default function PaymentHome() {
    const navigate = useNavigate();
    const [reservation, setReservation] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const currentUser = useSelector((state) => state.userInfo.userInfoList[0]);

    const {id} = useParams();

    useEffect(() => {
        const fetchReservation = async () => {
            if (!currentUser?.username) {
                alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
                navigate("/login");
                return;
            }

            try {
                const res = await apiClient.get("/book/list", {
                    params: {username: currentUser.username}
                });
                console.log("널확인", res.data);
                const formatted = res.data.map((item, index) => ({
                    id: item.id || 1000 + index,
                    accommodation: item.accomid,
                    reserverName: item.username || "사용자",
                    checkIn: item.checkindate,
                    checkOut: item.checkoutdate,
                    guests: item.guests || 1,
                    status: item.status || "예약",
                    address: item.accom?.address || "주소 없음",
                    detailaddr: item.accom?.detailaddr || "",
                    description: item.accom?.description || "",
                    price: item.totalPrice || null,
                    accom: item.accom || {},
                }));

                const matchingReservations = formatted.filter(
                    (r) => r.accommodation === Number(id)
                );

                if (matchingReservations.length > 0) {
                    const latestReservation = matchingReservations.reduce(
                        (latest, current) => current.id > latest.id ? current : latest
                    );
                    setReservation(latestReservation);
                } else {
                    setError("해당 숙소에 대한 예약 정보가 없습니다.");
                }

            } catch (error) {
                console.error("❌ 예약 정보 불러오기 실패: ", error);
                setError("예약 정보를 불러오는 데 실패했습니다.");
            }
        };

        fetchReservation();

    }, [currentUser?.username, navigate]);

    const handlePayment = async () => {
        setIsLoading(true);
        const data = {
            cid: "TC0ONETIME",
            partner_order_id: "order1234",
            partner_user_id: "user1234",
            item_name: "테스트 상품",
            quantity: 1,
            total_amount: reservation.price,
            tax_free_amount: 0,
            approval_url: "http://localhost:3000/payment/success",
            cancel_url: "http://localhost:3000/payment/cancel",
            fail_url: "http://localhost:3000/payment/fail"
        };
        try {
            const response = await kakaoClient.post(
                "/v1/payment/ready",
                qs.stringify(data)
            );

            const {tid, next_redirect_pc_url} = response.data;

            // ✅ 결제 승인 단계에서 사용할 TID 저장
            localStorage.setItem("kakao_tid", tid);
            localStorage.setItem("pendingReservation", JSON.stringify(reservation)); // 나중에 DB 저장용
            localStorage.setItem("username", currentUser.username)
            localStorage.setItem("bookid", reservation.id)

            // ✅ 카카오 결제창으로 리디렉션
            window.location.href = next_redirect_pc_url;
            navigate("/mypage/reservations");
        } catch (error) {
            console.error("❌ 결제 실패: ", error);
            alert("결제 요청 중 오류가 발생했습니다.");
        } finally {
            setIsLoading(false);
        }
    };

    if (error) {
        return (
            <div style={{padding: "20px"}}>
                <p style={{color: "red"}}>{error}</p>
                <button
                    onClick={() => navigate("/")}
                    style={{
                        padding: "10px 20px",
                        backgroundColor: "#FF5A5F",
                        color: "white",
                        border: "none",
                        borderRadius: "6px",
                        cursor: "pointer",
                    }}
                >
                    홈으로 돌아가기
                </button>
            </div>
        );
    }

    return (
        <div className="payment">
            <div
                className="payment-text">
                <h2>결제하기</h2>
                <br/>
                {!reservation ? (
                    <p>예약 정보를 불러오는 중입니다...</p>
                ) : (
                    <>
                        <p><strong>예약자:</strong> {currentUser?.username || "미로그인"}</p>
                        <p><strong>예약번호:</strong> {reservation.accommodation || "미로그인"}</p>
                        <p><strong>숙소:</strong> {reservation.accommodation}</p>
                        <p><strong>체크인:</strong> {reservation.checkIn}</p>
                        <p><strong>체크아웃:</strong> {reservation.checkOut}</p>
                        <p><strong>인원:</strong> {reservation.guests}</p>
                        <p><strong>총 금액:</strong> {reservation.price?.toLocaleString()}원</p>
                        <button
                            onClick={handlePayment}
                            disabled={isLoading}
                            style={{
                                padding: "10px 20px",
                                backgroundColor: isLoading ? "#ccc" : "#ce3d41",
                                color: "white",
                                border: "none",
                                borderRadius: "6px",
                                cursor: isLoading ? "not-allowed" : "pointer",
                                marginTop: "20px",
                            }}
                        >
                            {isLoading ? "결제 처리 중..." : "결제하기"}
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}
