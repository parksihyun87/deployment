import { useEffect, useState } from 'react';

const KakaoMap = ({ latitude, longitude }) => {
    const [mapLoaded, setMapLoaded] = useState(false);

    useEffect(() => {
        // Kakao SDK가 로드되었는지 확인
        const script = document.createElement('script');
        script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=fb324cc72112a45fa662ed7d393ce758&autoload=false`;
        script.onload = () => {
            window.kakao.maps.load(() => {
                setMapLoaded(true);
            });
        };
        document.head.appendChild(script);
    }, []);

    useEffect(() => {
        if (!mapLoaded) return;

        const container = document.getElementById('map');
        const options = {
            center: new window.kakao.maps.LatLng(latitude, longitude),
            level: 3,
        };
        const map = new window.kakao.maps.Map(container, options);

        const marker = new window.kakao.maps.Marker({
            position: map.getCenter(),
        });
        marker.setMap(map);
    }, [mapLoaded, latitude, longitude]);

    return <div id="map" style={{ width: '100%', height: '400px' }} />;
};

export default KakaoMap;
