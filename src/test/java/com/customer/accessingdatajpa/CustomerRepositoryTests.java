package com.customer.accessingdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OpRepoTest
public class CustomerRepositoryTests {
    @Autowired
    CustomerRepository repo;
    @Autowired
    EntityManager em;

    void flushAndClear() {
        em.flush();
        em.clear();
    }

    @Test
    void shouldFindCustomer() {
        repo.save( new Corporate( "Company", "company@gmail.com", 1234567890) );
        flushAndClear();
        Corporate c = (Corporate) repo.findByEmail("company@gmail.com").get( 0 );
        assertThat( c.getUen() ).isEqualTo( 1234567890 );
    }

    @Test
    void shouldUpdateCustomer() {
        repo.save(new Individual( "Ang", "aa@gmail.com", "S9876543A"));
        flushAndClear();
        Customer c = repo.findByUsername("Ang").get( 0 );
        assertThat( c.getEmail() ).isEqualTo( "aa@gmail.com" );
        c.setEmail("aaron-ang@gmail.com");
        repo.save(c);
        flushAndClear();
        Customer c2 = repo.findByUsername("Ang").get( 0 );
        assertThat( c2.getEmail() ).isEqualTo( "aaron-ang@gmail.com" );
    }

    @Test
    void shouldGetAllCustomers() {
		Customer c1 = new Individual("Ang", "aa@gmail.com", "S321A");
		Customer c2 = new Individual("Ong", "ao@gmail.com", "S654B");
		Customer c3 = new Individual("Tan", "at@gmail.com", "S987C");
		Customer c4 = new Corporate("Apple", "apple@gmail.com", 123);
		Customer c5 = new Corporate("Facebook", "facebook@gmail.com", 456);
		Customer c6 = new Corporate("Netflix", "netflix@gmail.com", 789);
		Customer[] cAll = {c1, c2, c3, c4, c5, c6};
		for (Customer c : cAll) {
			repo.save(c);
		}
		flushAndClear();
		List<Customer> result = (List<Customer>) repo.findAll();
        assertThat( result.size() ).isEqualTo(6);
    }

    @Test
    void shouldDeleteCustomer() {
        Customer c1 = new Individual( "Ang", "aa@gmail.com", "S135A");
        c1.addAccount(new Account("Savings", 10100));
        Customer c2 = new Individual( "Ong", "ao@gmail.com", "S246B");
        c2.addAccount(new Account("Savings", 100));
        Customer c3 = new Individual( "Tan", "at@gmail.com", "S369C");
        c3.addAccount(new Account("Savings", 22100));
        Customer c4 = new Corporate("Apple", "apple@gmail.com", 123);
        c4.addAccount(new Account("Current", 35100));
        Customer c5 = new Corporate("Facebook", "facebook@gmail.com", 456);
        c5.addAccount(new Account("Current", 1100));
        Customer c6 = new Corporate("Netflix", "netflix@gmail.com", 789);
        c6.addAccount(new Account("Current", 2100));
        Customer[] cAll = {c1, c2, c3, c4, c5, c6};
        for (Customer c : cAll) {
            repo.save(c);
        }
        flushAndClear();
        Customer c7 = repo.findByUsername("Ong").get( 0 );
        repo.delete(c7);
        flushAndClear();
        List<Customer> result = (List<Customer>) repo.findAll();
        assertThat( result.size() ).isEqualTo( 5 );
    }

    @Test
    void shouldAddNewAccount() {
        Customer c = new Individual( "Ang", "aa@gmail.com", "S987D");
        c.addAccount(new Account("Savings", 100));
        repo.save(c);
        flushAndClear();
        Customer c3 = repo.findByEmail("aa@gmail.com").get( 0 );
        assertThat(c3.getAccounts().size()).isGreaterThan( 0 );
    }

    @Test
    void shouldDepositMoney() {
        Customer c = new Individual( "Ang", "aa@gmail.com", "S654321E");
        c.addAccount(new Account("Savings", 100));
        repo.save(c);
        flushAndClear();
        Customer c2 = repo.findByEmail("aa@gmail.com").get( 0 );
        Account a2 = c2.getAccounts().get( 0 );
        assertThat(a2.getBalance()).isEqualTo( 100 );
        a2.deposit(200);
        repo.save(c2);
        flushAndClear();
        Account a3 = repo.findByEmail("aa@gmail.com").get( 0 ).getAccounts().get( 0 );
        assertThat(a3.getBalance()).isEqualTo( 300 );
    }

    @Test
    void shouldWithdrawMoney() {
        Customer c = new Corporate( "Apple", "aa@gmail.com", 98765);
        c.addAccount(new Account("Savings", 100));
        repo.save(c);
        flushAndClear();
        Customer c2 = repo.findByEmail("aa@gmail.com").get( 0 );
        Account a2 = c2.getAccounts().get( 0 );
        assertThat(a2.getBalance()).isEqualTo( 100 );
        a2.withdraw(50);
        repo.save(c2);
        flushAndClear();
        Account a3 = repo.findByEmail("aa@gmail.com").get( 0 ).getAccounts().get( 0 );
        assertThat(a3.getBalance()).isEqualTo( 50 );
    }

    @Test
    void shouldGetRichCustomersBasedOnAnyAccount() {
        Customer c1 = new Individual( "Ang", "aa@gmail.com", "S1234A");
        c1.addAccount(new Account("Current", 6000));
        Customer c2 = new Individual( "Ong", "ao@gmail.com","S2345B");
        c2.addAccount(new Account("Savings", 100));
        Customer c3 = new Individual( "Tan", "at@gmail.com","S3456C");
        c3.addAccount(new Account("Savings", 22100));
        Customer c4 = new Corporate( "Apple", "al@gmail.com", 1234347);
        c4.addAccount(new Account("Current", 35100));
        Customer c5 = new Corporate("Banana", "bl@gmail.com", 1231234);
        c5.addAccount(new Account("Current", 1100));
        Customer c6 = new Corporate("Carrot", "cl@gmail.com", 1247234);
        c3.addAccount(new Account("Current", 2200));
        Customer[] cAll = {c1, c2, c3, c4, c5, c6};
        for (Customer c : cAll) {
            repo.save(c);
        }
        flushAndClear();
        assertThat( repo.getRichCustomers( 10000 ) ).hasSize( 2 );
    }

    @Test
    void shouldGetRichCustomersBasedOnTotalBalance() {
        Customer c1 = new Individual( "Ang", "aa@gmail.com", "S12345A");
        c1.addAccount(new Account("Savings", 5100));
        c1.addAccount(new Account("Current", 6000));
        Customer c2 = new Individual( "Ong", "ao@gmail.com","S34125B");
        c2.addAccount(new Account("Savings", 100));
        Customer c3 = new Individual( "Tan", "at@gmail.com","S51234C");
        c3.addAccount(new Account("Savings", 22100));
        c3.addAccount(new Account("Current", 100));
        Customer c4 = new Corporate( "Apple", "al@gmail.com", 13234546);
        c4.addAccount(new Account("Savings", 35100));
        Customer c5 = new Corporate("Blackberry", "bl@gmail.com",456789876);
        c5.addAccount(new Account("Savings", 1100));
        Customer c6 = new Corporate("Company", "cl@gmail.com",34544567);
        c3.addAccount(new Account("Current", 2200));
        c6.addAccount(new Account("Savings", 2100));
        Customer[] cAll = {c1, c2, c3, c4, c5, c6};
        for (Customer c : cAll) {
            repo.save(c);
        }
        flushAndClear();
        List<Customer> list = repo.getRichCustomers( 10000 );
        Customer c = list.get(0);
        List<Account> accounts = c.getAccounts();
        Account a = accounts.get( 0 );
        System.out.println( a.getBalance() );
    }

    @Test
    void shouldSortByCustomerWealth() {
        Customer c1 = new Individual( "Ang", "aa@gmail.com", "13245hrewt4g");
        c1.addAccount(new Account("Savings", 5100));
        c1.addAccount(new Account("Current", 6000));
        Customer c2 = new Individual( "Ong", "ao@gmail.com", "3145ga4ty");
        c2.addAccount(new Account("Savings", 100));
        Customer c3 = new Individual( "Tan", "at@gmail.com", "134f3t5yh");
        c3.addAccount(new Account("Savings", 22100));
        c3.addAccount(new Account("Current", 100));
        Customer c4 = new Corporate( "Lim", "al@gmail.com", 4767572);
        c4.addAccount(new Account("Savings", 35100));
        Customer c5 = new Corporate("Lim", "bl@gmail.com", 7725476);
        c5.addAccount(new Account("Savings", 1100));
        Customer c6 = new Corporate("Lim", "cl@gmail.com", 7254767);
        c3.addAccount(new Account("Current", 2200));
        c6.addAccount(new Account("Savings", 2100));
        Customer[] cAll = {c1, c2, c3, c4, c5, c6};
        for (Customer c : cAll) {
            repo.save(c);
        }
        flushAndClear();
        List<Customer> customers = repo.sortByWealth();
        assertThat( customers.get( 0 ).getEmail() ).isEqualTo("al@gmail.com");
        assertThat( customers.get( 1 ).getEmail() ).isEqualTo("at@gmail.com");
        assertThat( customers.get( 2 ).getEmail() ).isEqualTo("aa@gmail.com");
        assertThat( customers.get( 3 ).getEmail() ).isEqualTo("cl@gmail.com");
        assertThat( customers.get( 4 ).getEmail() ).isEqualTo("bl@gmail.com");
        assertThat( customers.get( 5 ).getEmail() ).isEqualTo("ao@gmail.com");
    }

    @Test
    void shouldGetCustomerAddress() {
        Customer c = new Individual( "Ang", "aa@gmail.com", "4g35y6");
        Address a = new Address("sesame street","city hall", 123456);
        c.setAddress( a );
        repo.save( c );
        flushAndClear();
        Customer c2 = repo.findByUsername("Ang").get( 0 );
        assertThat(c2.getAddress()).isEqualToComparingFieldByField(a);
    }

    @Test
    void shouldUpdateCustomerAddress() {
        Customer c = new Individual( "Ang", "aa@gmail.com", "12e3df45t");
        Address a = new Address("sesame street","city hall", 123456);
        c.setAddress( a );
        repo.save( c );
        flushAndClear();
        Customer c2 = repo.findByUsername("Ang").get( 0 );
        Address a2 = new Address("Bourke Street", "Melbourne", 654321);
        c2.setAddress(a2);
        repo.save(c2);
        flushAndClear();
        Customer c3 = repo.findByUsername("Ang").get( 0 );
        assertThat(c3.getAddress()).isEqualToComparingFieldByField(a2);
    }

    @Test
    void shouldFindCustomerByIdNumber() {
        Customer c = new Individual("Apple", "apple@apple.com", "S12345C");
        repo.save(c);
        flushAndClear();
        Individual i =  (Individual) repo.findByIdNumber("S12345C").get( 0 );
        assertThat( i.getIdNumber() ).isEqualTo( "S12345C" );
    }

    @Test
    void shouldFindCustomerByUEN() {
        Customer c = new Corporate("Apple", "apple@apple.com", 12345);
        repo.save(c);
        flushAndClear();
        Corporate i =  (Corporate) repo.findByUen(12345).get( 0 );
        assertThat( i.getUen() ).isEqualTo( 12345 );
    }

    @Test
    void shouldGetRichCustomersByTheirType() {
        Customer c1 = new Individual( "Ang", "aa@gmail.com", "S12345A");
        c1.addAccount(new Account("Savings", 5100));
        c1.addAccount(new Account("Current", 6000));
        Customer c2 = new Individual( "Ong", "ao@gmail.com","S34125B");
        c2.addAccount(new Account("Savings", 100));
        Customer c3 = new Individual( "Tan", "at@gmail.com","S51234C");
        c3.addAccount(new Account("Savings", 22100));
        c3.addAccount(new Account("Current", 100));
        Customer c4 = new Corporate( "Apple", "al@gmail.com", 13234546);
        c4.addAccount(new Account("Savings", 35100));
        Customer c5 = new Corporate("Blackberry", "bl@gmail.com",456789876);
        c5.addAccount(new Account("Savings", 1100));
        Customer c6 = new Corporate("Company", "cl@gmail.com",34544567);
        c6.addAccount(new Account("Current", 2200));
        c6.addAccount(new Account("Savings", 2100));
        Customer[] cAll = {c1, c2, c3, c4, c5, c6};
        for (Customer c : cAll) {
            repo.save(c);
        }
        flushAndClear();
        assertThat( repo.getRichByType( 10000, "Individual" ) ).hasSize( 2 );
        flushAndClear();
        assertThat( repo.getRichByType( 4000, "Corporate" ) ).hasSize( 2 );
    }

//    @Test
//    void shouldThrowErrorWhenTryingToDoSimultaneousUpdates() {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AccessingDataJpaApplication.class);
//        ExecutorService es = Executors.newFixedThreadPool(2);
//        Customer c = new Corporate("Dell", "dell@gmail.com", 3103);
//        repo.save(c);
////      user 1
//        es.execute(() -> {
//            Customer c3 = repo.findByEmail("dell@gmail.com").get(0);
//            System.out.println("user1 reading: " + c3);
//            c3.setUsername( "AUS" );
////                //little delay
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    repo.save(c3);
//                } catch (Exception e) {
//                    System.err.println("user1 " + e);
//                    System.out.println("user1 after error: " + repo.findByEmail("dell@gmail.com"));
//                    return;
//                }
//                System.out.println("user1 finished: " + repo.findByEmail("dell@gmail.com"));
//        });
////      user 2
//        es.execute(() -> {
//            Customer c4 = repo.findByEmail("dell@gmail.com").get(0);
//            System.out.println("user2 reading: " + c4);
//            c4.setUsername( "JAPAN" );
//            try {
//                repo.save(c4);
//            } catch (Exception e) {
//                System.err.println("user2: " + e);
//                System.out.println("user2 after error: " + repo.findByEmail("dell@gmail.com"));
//                return;
//            }
//            System.out.println("user2 finished: " + repo.findByEmail("dell@gmail.com"));
//        });
//        es.shutdown();
//        try {
//            es.awaitTermination(10, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
//        emf.close();
//    }
}
