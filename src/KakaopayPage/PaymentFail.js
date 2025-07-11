// src/pages/PaymentFail.jsx
import { useNavigate } from "react-router-dom";

export default function PaymentFail() {
    const navigate = useNavigate();

    return (
        <div style={{ padding: "20px" }}>
            <h2>결제 실패</h2>
            <p>결제에 실패했습니다. 다시 시도해 주세요.</p>
            <button onClick={() => navigate("/")}>홈으로</button>
        </div>
    );
}
