import {NavLink, Outlet, useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import {useEffect} from "react";

export default function MyInfoLayout() {
    const navigate = useNavigate();
    const currentUser = useSelector(state => state.userInfo.userInfoList[0]);


    useEffect(() => {
        if (!currentUser) {
            navigate("/login");
        }
    },[navigate, currentUser]);


    return (
        <div>
            <h2>내 정보</h2>
            <nav>
                <NavLink
                    to="profile"
                    style={({ isActive }) => ({ fontWeight: isActive ? "bold" : "normal" })}
                >
                    내 정보 확인/수정
                </NavLink>{" "}
                |{" "}
                <NavLink
                    to="reservations"
                    style={({ isActive }) => ({ fontWeight: isActive ? "bold" : "normal" })}
                >
                    예약 정보
                </NavLink>{" "}
                |{" "}
                <NavLink
                    to="myroom"
                    style={({ isActive }) => ({ fontWeight: isActive ? "bold" : "normal" })}
                >
                    이용 내역
                </NavLink>{" "}
                |{" "}
                <NavLink
                    to="quit"
                    style={({ isActive }) => ({ fontWeight: isActive ? "bold" : "normal" })}
                >
                    회원 탈퇴
                </NavLink>
            </nav>
            <hr />
            <Outlet />
        </div>
    );
}