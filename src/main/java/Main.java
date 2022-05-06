
import domain.Sido;
import domain.Sigungu;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dao.SidoDAO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {

        DBInit dbInit = new DBInit();
        dbInit.initRegion();
        dbInit.initPopulation();

        LocalDate startDate = LocalDate.of(2022, 1, 1); // dayOfMonth는 무조건 1로 설정
        LocalDate endDate = LocalDate.of(2022,5,1); // dayOfMonth는 무조건 1로 설정
        String serviceKey = ""; //인증키 번호


        insertApartmentData(startDate,endDate,serviceKey,dbInit); // startDate ~ endDate 기간 동안의 전국 아파트 실거래 데이터 저장
        insertRowHouse(startDate,endDate,serviceKey,dbInit); // startDate ~ endDate 기간 동안의 전국 연립다세대 실거래 데이터 저장
        insertDetachedHouse(startDate,endDate,serviceKey,dbInit); // startDate ~ endDate 기간 동안의 전국 단독/다가구 실거래 데이터 저장


////       dbInit.initApartment(startDate,endDate,"11110",serviceKey);
////       dbInit.initRowHouse(startDate,endDate,"11110",serviceKey);
////       dbInit.initDetachedHuse(startDate,endDate,"11110", serviceKey);

        annotationBaseCode(); //어노테이션 기반 mybatis 테스트(시도 테이블 전체 출력)

    }

    public static void insertApartmentData(LocalDate startDate,LocalDate endDate,String serviceKey,DBInit dbInit){
        log.info("아파트 데이터 저장");
        List<Sigungu> sigungus = dbInit.getSigungu();
        for (Sigungu sigungu : sigungus) {
            log.info("지역코드={} 시군구={}",sigungu.getRegionalCode(),sigungu.getRegionName());
                dbInit.initApartment(startDate,endDate,sigungu.getRegionalCode(),serviceKey);
        }
    }

    public static void insertRowHouse(LocalDate startDate,LocalDate endDate,String serviceKey,DBInit dbInit){
        log.info("연립다세대 데이터 저장");
        List<Sigungu> sigungus = dbInit.getSigungu();
        for (Sigungu sigungu : sigungus) {
            log.info("지역코드={} 시군구={}",sigungu.getRegionalCode(),sigungu.getRegionName());
            dbInit.initRowHouse(startDate,endDate,sigungu.getRegionalCode(),serviceKey);
        }
    }

    public static void insertDetachedHouse(LocalDate startDate,LocalDate endDate,String serviceKey,DBInit dbInit){
        log.info("단독/다가구 데이터 저장");
        List<Sigungu> sigungus = dbInit.getSigungu();
        for (Sigungu sigungu : sigungus) {
            log.info("지역코드={} 시군구={}",sigungu.getRegionalCode(),sigungu.getRegionName());
            dbInit.initDetachedHouse(startDate,endDate,sigungu.getRegionalCode(),serviceKey);
        }
    }

    private static void annotationBaseCode() {
        SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();

        SidoDAO sidoDAO = new SidoDAO(sqlSessionFactory);
        List<Sido> allSido = sidoDAO.findAllSido();
        for (Sido sido : allSido) {
            System.out.println(sido.getRegionalCode() + sido.getRegionName());
        }
    }
}
