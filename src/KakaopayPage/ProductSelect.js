import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {resetFilters, setFilters, setAccom} from "./store";
import './App.css'
import {kakaoClient} from "../util/kakaoInstatance";

export default async function ProductSelect() {
// 예시 (form-urlencoded로 변환 필요)
    import qs from "qs";

    const data = {
        cid: "TC0ONETIME",
        partner_order_id: "order1234",
        partner_user_id: "user1234",
        item_name: "테스트 상품",
        quantity: 1,
        total_amount: 1000,
        tax_free_amount: 0,
        approval_url: "https://yourdomain.com/success",
        cancel_url: "https://yourdomain.com/cancel",
        fail_url: "https://yourdomain.com/fail",
    };

    const response = await kakaoClient.post(
        "/v1/payment/ready",
        qs.stringify(data)
    );

    const {tid, next_redirect_pc_url} = response.data;


    return (
        <>
        </>
    )
}