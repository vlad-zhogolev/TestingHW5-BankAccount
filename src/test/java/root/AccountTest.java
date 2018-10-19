package root;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ObjectInputStream;

import static org.testng.Assert.*;

/**
 * Created by ZV on 19.10.2018.
 */
public class AccountTest extends Account {
    @Test(dataProvider = "depositDataProvider")
    public void testDeposit(boolean blocked, int bound, int balance, int maxCredit, int sum,
                            boolean expectedResult, int expectedBalance) throws Exception {
        Account account = new Account();
        account.blocked = blocked;
        account.bound = bound;
        account.balance = balance;
        account.maxCredit = maxCredit;
        boolean result = account.deposit(sum);
        assertEquals(result, expectedResult);
        assertEquals(account.balance, expectedBalance);
    }

    @DataProvider(name = "depositDataProvider")
    public Object[][] depositTestData() {
        int bound = 1000000;
        int balance = 0;
        int maxCredit = -1000;
        return new Object[][]{
                //{blocked, bound, balance, maxCredit, sum, expectedReturn, expectedBalance}
                {true, bound, balance, maxCredit, 100, false, 0},
                {false, bound, balance, maxCredit, -1, false, 0},
                {false, bound, balance, maxCredit, 0, true, 0},
                {false, bound, balance, maxCredit, 1, true, 1},
                {false, bound, balance, maxCredit, 100, true, 100},
                {false, bound, balance, maxCredit, bound - 1, true, bound - 1},
                {false, bound, balance, maxCredit, bound, true, bound},
                {false, bound, balance, maxCredit, bound + 1, false, 0}
        };
    }

    @Test(dataProvider = "withdrawDataProvider")
    public void testWithdraw(boolean blocked, int bound, int balance, int maxCredit, int sum,
                             boolean expectedResult, int expectedBalance) throws Exception {
        Account account = new Account();
        account.blocked = blocked;
        account.bound = bound;
        account.balance = balance;
        account.maxCredit = maxCredit;
        boolean result = account.withdraw(sum);
        assertEquals(result, expectedResult);
        assertEquals(account.balance, expectedBalance);
    }

    @DataProvider(name = "withdrawDataProvider")
    public Object[][] withdrawTestData() {
        int bound = 1000000;
        int balance = 0;
        int maxCredit = -1000;
        return new Object[][]{
                //{blocked, bound, balance, maxCredit, sum, expectedReturn, expectedBalance}
                {true, bound, balance, maxCredit, 100, false, 0},
                {false, bound, balance, maxCredit, -1, false, 0},
                {false, bound, balance, maxCredit, bound + 1, false, 0},
                {false, bound, balance, maxCredit, balance - maxCredit - 1, true, maxCredit + 1},
                {false, bound, balance, maxCredit, balance - maxCredit, false, balance},
                {false, bound, balance, maxCredit, 0, true, 0},
                {false, bound, balance, maxCredit, 1, true, -1},
                {false, bound, balance, maxCredit, 100, true, -100},
                {false, bound, bound, maxCredit, bound - 1, true, 1},
                {false, bound, bound, maxCredit, bound, true, 0}
        };
    }

    @Test(dataProvider = "balanceTestData")
    public void testGetBalance(int balance, int expectedBalance) throws Exception {
        Account account = new Account();
        account.balance = balance;
        assertEquals(account.getBalance(), expectedBalance);
    }

    @DataProvider(name = "balanceTestData")
    public Object[][] balanceTestData() {
        return new Object[][]{
                {1, 1},
                {-1, -1},
                {0, 0}
        };
    }

    @Test(dataProvider = "maxCreditTestData")
    public void testGetMaxCredit(int maxCredit, int expectedMaxCredit) throws Exception {
        Account account = new Account();
        account.maxCredit = maxCredit;
        assertEquals(account.getMaxCredit(), expectedMaxCredit);
    }

    @DataProvider(name = "maxCreditTestData")
    public Object[][] getMaxCreditTestData() {
        return new Object[][]{
                {-100, 100},
                {-20, 20}
        };
    }

    @Test
    public void testIsBlocked() throws Exception {
        Account account = new Account();
        assertTrue(!account.isBlocked());
        account.block();
        assertTrue(account.isBlocked());
    }

    @Test
    public void testBlock() throws Exception {
        Account account = new Account();
        account.block();
        assertEquals(account.blocked, true);
    }

    @Test
    public void testUnblock() throws Exception {
        Account account = new Account();
        account.block();
        assertTrue(account.isBlocked());
        assertTrue(account.unblock());
        assertFalse(account.isBlocked());
        account.balance = account.maxCredit - 1;
        assertFalse(account.unblock());
    }

    @Test(dataProvider = "setMaxCreditDataProvider")
    public void testSetMaxCredit(int bound, int mc, boolean expectedResult) throws Exception {
        Account account = new Account();
        account.bound = bound;
        account.block();
        boolean result = account.setMaxCredit(mc);
        assertEquals(result, expectedResult);
        if(result)
            assertEquals(account.maxCredit, -mc);
        //test 3 requirement (they will give false on some tests, because class doesn't
        //fit 3 requirement
        account.unblock();
        int maxCreditBefore = account.getMaxCredit();
        result = account.setMaxCredit(mc);
        assertFalse(result);
        assertEquals(maxCreditBefore, account.getMaxCredit());
    }

    @DataProvider(name = "setMaxCreditDataProvider")
    public Object[][] setMaxCreditDataProvider(){
        int bound = 1000000;
        return new Object[][]{
                {bound, 10, true},
                {bound, 100, true},
                {bound, bound - 1, true},
                {bound, bound, true},
                {bound, bound + 1, false},
                {bound, -bound - 1, false}
        };
    }
}