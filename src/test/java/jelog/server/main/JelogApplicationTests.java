package jelog.server.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JUnit 제공 어노텐션
 * @Test : 메서드가 테스트 메서드임을 나타냅니다.
 * @ParameterizedTest : 메서드가 매개 변수가 있는 테스트임을 나타냅니다.
 * @RepeatedTest : 반복 테스트를 위한 메소드임을 나타냅니다.
 * @TestFactory : 동적 테스트를 위한 테스트 팩토리 메소드를 나타냅니다.
 * @TestTemplate : 여러번 호출되도록 설계된 테스트 케이스의 템플릿임을 나타냅니다.
 * @TestMethodOrder : 테스트 메소드 실생 순서를 구성하는데 사용됩니다.
 * @TestInstance : 테스트 라이프 사이플을 구성하는데 사용됩니다.
 * @DisplayName : 테스트 클래스 또는 테스트 메서드에 대한 사용자 지정 표시 이름을 설정합니다.
 * @DisplayNameGeneration : 테스트 클래스에 대한 커스텀 이름 생성기를 생성합니다.
 * @BeforeEach : 모든 테스트 메소드 실행 전에 실행되는 메소드입니다.
 * @AfterEach : 모든 테스트 메소드 실행이 끝나면 실행되는 메소드입니다.
 * @BeforeAll : 하나의 테스트 실행 전에 실행되는 메소드입니다.
 * @AfterAll : 하나의 테스트 실행이 끝나면 실행되는 메소드입니다.
 * @Nested : non-static 중첩 클래스임을 나타냅니다.
 * @Tag : 새로운 태그를 선언할 때 사용됩니다.
 * @Disable : 테스트 클래스나 테스트 메소드를 사용하지 않도록 할 때 사용됩니다.
 * @Timeout : 주어진 시간안에 실행을 못할 경우 실패하도록 하는데 사용됩니다.
 * */

@SpringBootTest
class JelogApplicationTests {

    @Test
    void contextLoads() {
    }

}
