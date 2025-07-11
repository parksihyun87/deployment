// src/pages/PaymentCancel.jsx
import { useNavigate } from "react-router-dom";

export default function PaymentCancel() {
    const navigate = useNavigate();

    return (
        <div style={{ padding: "20px" }}>
            <h2>결제 취소</h2>
            <p>사용자에 의해 결제가 취소되었습니다.</p>
            <button onClick={() => navigate("/")}>홈으로</button>
        </div>
    );
}
