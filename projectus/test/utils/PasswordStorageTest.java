package utils;
/** based on https://raw.githubusercontent.com/defuse/password-hashing/master/tests/Test.java */

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;


public class PasswordStorageTest {

    // Make sure truncated hashes don't validate.
    @Test
    public void truncatedHashTest() {
        String userString = "password!";
        String goodHash = "";
        String badHash = "";
        int badHashLength = 0;

        try {
            goodHash = PasswordStorage.createHash(userString);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        badHashLength = goodHash.length();

        do {
            badHashLength -= 1;
            badHash = goodHash.substring(0, badHashLength);

            boolean raised = false;
            try {
                PasswordStorage.verifyPassword(userString, badHash);
            } catch (PasswordStorage.InvalidHashException ex) {
                raised = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                fail();
            }

            if (!raised) {
                fail("Truncated hash test: FAIL " +
                        "(At hash length of " +
                        badHashLength + ")"
                );
            }

            // The loop goes on until it is two characters away from the last : it
            // finds. This is because the PBKDF2 function requires a hash that's at
            // least 2 characters long.
        } while (badHash.charAt(badHashLength - 3) != ':');
    }

    // Tests the basic functionality of the PasswordStorage class
    @Test(timeout = 30000)
    public void basicTests() {
        try {
            // Test password validation
            for (int i = 0; i < 5; i++) {
                String password = "" + i;
                String hash = PasswordStorage.createHash(password);
                String secondHash = PasswordStorage.createHash(password);
                if (hash.equals(secondHash)) {
                    fail("FAILURE: TWO HASHES ARE EQUAL!");
                }
                String wrongPassword = "" + (i + 1);
                if (PasswordStorage.verifyPassword(wrongPassword, hash)) {
                    fail("FAILURE: WRONG PASSWORD ACCEPTED!");
                }
                if (!PasswordStorage.verifyPassword(password, hash)) {
                    fail("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test(expected = PasswordStorage.CannotPerformOperationException.class)
    public void testHashFunctionChecking() throws PasswordStorage.CannotPerformOperationException {
        String hash = PasswordStorage.createHash("foobar");
        hash = hash.replaceFirst("sha1:", "sha256:");

        try {
            PasswordStorage.verifyPassword("foobar", hash);

        } catch (PasswordStorage.InvalidHashException e) {
            e.printStackTrace();
        }
    }

    /**
     * The verification of a hash should be slow enough but no detectable during a single operation.
     */

    @Test(timeout = 15000)
    public void timeToVerifyTest() {
        long verifMinTimeMs = 10;
        long verifMaxTimeMs = 700;

        long numberOfHash = 5;
        try {
            List<String> hashs = new ArrayList<>();
            for (int i = 0; i < numberOfHash; i++) {
                hashs.add(PasswordStorage.createHash("foobar"));
            }

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < numberOfHash; i++) {
                PasswordStorage.verifyPassword("foobar", hashs.get(i));
            }
            long endTime = System.currentTimeMillis();
            long timePerVerif = (endTime-startTime)/numberOfHash;

            assertTrue(String.format("the password verification is too fast (%d < %dms/verif), " +
                    "you should augment the constant PBKDF2_ITERATIONS", timePerVerif, verifMinTimeMs),
                    timePerVerif >= verifMinTimeMs);

            assertTrue(String.format("the password verification is too slow (%d > %dms/verif), " +
                    "you should reduce the constant PBKDF2_ITERATIONS", timePerVerif, verifMaxTimeMs),
                    timePerVerif <= verifMaxTimeMs);

            System.out.println(String.format("time per verif was %d ms", timePerVerif));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
