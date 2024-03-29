package ResourceManager;

import TransactionManager.TransactionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by brian on 06/10/15.
 */
public class ResourceManagerRunnable implements Runnable, ResourceManager {
    Socket clientSocket = null;
    PrintWriter toClient;
    BufferedReader fromClient;
    TransactionManager tm;

    public boolean RMDieAfterRequest = false;
    public boolean RMDieAfterDecision = false;
    public boolean RMDieAfterSendAnswer = false;
    public boolean RMDieBeforeCommitAbort = false;


    public void setRMDieAfterRequest() {
        RMDieAfterRequest = true;
        RMDieAfterDecision = false;
        RMDieAfterSendAnswer = false;
        RMDieBeforeCommitAbort = false;
    }

    public void setRMDieAfterDecision() {
        RMDieAfterRequest = false;
        RMDieAfterDecision = true;
        RMDieAfterSendAnswer = false;
        RMDieBeforeCommitAbort = false;
    }

    public void setRMDieAfterSendAnswer() {
        RMDieAfterRequest = false;
        RMDieAfterDecision = false;
        RMDieAfterSendAnswer = true;
        RMDieBeforeCommitAbort = false;
    }

    public void setRMDieBeforeCommitAbort() {
        RMDieAfterRequest = false;
        RMDieAfterDecision = false;
        RMDieAfterSendAnswer = false;
        RMDieBeforeCommitAbort = true;
    }
    
    

    public ResourceManagerRunnable(Socket clientSocket, String serverType) {
        this.clientSocket = clientSocket;
        setComms();
    }

    private void setComms() {
        try {
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line;
        try {
            try {
                outerloop:
                while (!(line = fromClient.readLine()).contains("END")) {
                    String[] cmdWords = line.split(",");
                    int choice = findChoice(cmdWords);

                    boolean success;
                    int value;
                    String stringValue;
                    switch (choice) {
                        case 2:
                            if (cmdWords.length < 4) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = addFlight(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]),
                                    Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 3:
                            if (cmdWords.length < 4) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = addCars(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 4:
                            if (cmdWords.length < 4) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = addRooms(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 5:
                            if (cmdWords.length < 1) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = newCustomer(Integer.parseInt(cmdWords[1]));
                            toClient.println(value);
                            break;
                        case 6:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = deleteFlight(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 7:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = deleteCars(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 8:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = deleteRooms(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 9:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = deleteCustomer(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 10:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryFlight(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            toClient.println(value);
                            break;
                        case 11:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryCars(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            toClient.println(value);
                            break;
                        case 12:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryRooms(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            toClient.println(value);
                            break;
                        case 13:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            stringValue = queryCustomerInfo(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            toClient.println(stringValue);
                            break;
                        case 14:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryFlightPrice(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            toClient.println(value);
                            break;
                        case 15:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryCarsPrice(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            toClient.println(value);
                            break;
                        case 16:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryRoomsPrice(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            toClient.println(value);
                            break;
                        case 17:
                            if (cmdWords.length < 3) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = reserveFlight(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]), Integer.parseInt(cmdWords[3]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 18:
                            if (cmdWords.length < 3) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = reserveCar(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]), cmdWords[3]);
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 19:
                            if (cmdWords.length < 3) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = reserveRoom(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]), cmdWords[3]);
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 20:

                        case -1:
                            toClient.println("ERROR :  Command " + cmdWords[0] + " not supported");
                            break;

                        case 21:
                            break outerloop;
                        case 22:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = newCustomerId(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 23:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = increaseReservableItemCount(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 24:
                            if (cmdWords.length < 5) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = undoAddFlight(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]), Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]), Integer.parseInt(cmdWords[5]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 25:
                            if (cmdWords.length < 5) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = undoAddCars(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]), Integer.parseInt(cmdWords[5]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 26:
                            if (cmdWords.length < 5) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = undoAddRooms(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]), Integer.parseInt(cmdWords[5]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 27:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryFlightReserved(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            toClient.println(value);
                            break;
                        case 28:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryCarsReserved(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            toClient.println(value);
                            break;
                        case 29:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            value = queryRoomsReserved(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            toClient.println(value);
                            break;
                        case 30:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = isExistingFlight(Integer.parseInt(cmdWords[1]), Integer.parseInt(cmdWords[2]));
                            if (success) {
                                toClient.println("trueExistingFlight");
                            } else toClient.println("false");
                            break;
                        case 31:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = isExistingCars(Integer.parseInt(cmdWords[1]), cmdWords[2]);
                            if (success) {
                                toClient.println("true");
                            } else toClient.println("false");
                            break;
                        case 32:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = isExistingRooms(Integer.parseInt(cmdWords[1]), (cmdWords[2]));
                            if (success) {
                                toClient.println("true");
                            } else toClient.println("false");
                            break;
                        case 33:
                            if (cmdWords.length < 2) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            success = decreaseReservableItemCount(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]));
                            if (success) toClient.println("true");
                            else toClient.println("false");
                            break;
                        case 63:
                            if (cmdWords.length != 1) {
                                toClient.println("ERROR: wrong arguments");
                                break;
                            }

                            //@crash
                            if (RMDieBeforeCommitAbort) {
                                Trace.error("RM die after receiving decision but before commit/abort");
                                System.exit(0);
                            }

                            if (cmdWords[0].contains("abortA")) {
                                success = loadMemoryFromDisk("A");
                            } else if (cmdWords[0].contains("abortB")) {
                                success = loadMemoryFromDisk("B");
                            } else success = false;
                            if (success) {
                                TCPServer.diskOperator.writeLogRecord("ABORT");
                                toClient.println("true");
                            } else {
                                toClient.println("false");
                            }
                            break;
                        case 64:
                            if (cmdWords.length != 1) {
                                toClient.println("ERROR: wrong arguments");
                                break;
                            }
                            success = false;
                            if (cmdWords[0].contains("A")) {
                                success = writeMainMemoryToDisk("A");

                            } else if (cmdWords[0].contains("B")) {
                                success = writeMainMemoryToDisk("B");
                            }
                            if (success) {
                                toClient.println("true");
                            } else {
                                toClient.println("false");
                            }

                            //@crash
                            if (RMDieAfterSendAnswer) {
                                Trace.error("RM die after deciding but before sending answer");
                                System.exit(0);
                            }

                            break;
                        case 65:
                            if (cmdWords.length < 6) {
                                toClient.println("ERROR : wrong arguments");
                                break;
                            }
                            writeCompleteData(Integer.parseInt(cmdWords[1]), cmdWords[2], Integer.parseInt(cmdWords[3]), Integer.parseInt(cmdWords[4]), Integer.parseInt(cmdWords[5]));
                            toClient.println("true");
                            break;
                        case 66:
                            System.exit(0);
                            break;
                        case 67:
                            //@crash
                            if (RMDieBeforeCommitAbort) {
                                Trace.error("RM die after receiving decision but before commit/abort");
                                System.exit(0);
                            }

                            if(cmdWords[0].equalsIgnoreCase("commita")) {
                                success = loadMemoryFromDisk("A");
                                if (success) {
                                    TCPServer.diskOperator.writeLogRecord("COMMIT");
                                    toClient.println("true");
                                } else {
                                    toClient.println("false");
                                }
                            } else if (cmdWords[0].equalsIgnoreCase("commitb")) {
                                success = loadMemoryFromDisk("B");
                                if (success) {
                                    TCPServer.diskOperator.writeLogRecord("COMMIT");
                                    toClient.println("true");
                                } else {
                                    toClient.println("false");
                                }
                            }
                            break;
                        case 68:
                            if (cmdWords.length != 1) {
                                toClient.println("ERROR: wrong arguments");
                                break;
                            }
                            if (cmdWords[0].contains("loadA")) {
                                success = loadMemoryFromDisk("A");
                            } else if (cmdWords[0].contains("loadB")) {
                                success = loadMemoryFromDisk("B");
                            } else success = false;
                            break;
                        case 69:
                            if (cmdWords[0].equals("rmdieafterrequest")) {
                                setRMDieAfterRequest();
                                Trace.warn("rm will die after receiving vote request but before sending an answer");
                            }
                            else if (cmdWords[0].equals("rmdieafterdecision")) {
                                setRMDieAfterDecision();
                                Trace.warn("rm will die after making a decision on preparedness but before sending an" +
                                        " answer");

                            }
                            else if (cmdWords[0].equals("rmdieaftersendanswer")) {
                                setRMDieAfterSendAnswer();
                                Trace.warn("rm will die after sending an answer");
                            }
                            else if (cmdWords[0].equals("rmdiebeforecommitabort")) {
                                setRMDieBeforeCommitAbort();
                                Trace.warn("rm will die after receiving decision but before executing decision");
                            }
                            break;
                        default:
                            toClient.println("ERROR :  Command " + cmdWords[0] + " not supported");
                            break;
                    }
                }
                Trace.info("a Middleware client thread disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean loadMemoryFromDisk(String shadowVersion) {
        if (TCPServer.serverType.equals("FLIGHT_RM")) {
            try {
                TCPServer.m_itemHT_flight = (RMHashtable) TCPServer.diskOperator.getDataFromDisk("flight" + shadowVersion);
                Trace.info("FLIGHT_RM loaded shadowVersion" + shadowVersion);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } else if (TCPServer.serverType.equals("CAR_RM")) {
            try {
                TCPServer.m_itemHT_car = (RMHashtable) TCPServer.diskOperator.getDataFromDisk("car" + shadowVersion);
                Trace.info("CAR_RM loaded shadowVersion" + shadowVersion);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } else if (TCPServer.serverType.equals("ROOM_RM")) {
            try {
                TCPServer.m_itemHT_room = (RMHashtable) TCPServer.diskOperator.getDataFromDisk("room" + shadowVersion);
                Trace.info("ROOM_RM loaded shadowVersion" + shadowVersion);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private boolean writeMainMemoryToDisk(String shadowVersion) {
        TCPServer.diskOperator.clearLogRecord();
        if (TCPServer.serverType.equals("FLIGHT_RM")) {
            try {
                TCPServer.diskOperator.writeDataToDisk(TCPServer.m_itemHT_flight, "flight" + shadowVersion);
                TCPServer.diskOperator.writeLogRecord("YES,");

                //@crash
                if (RMDieAfterDecision) {
                    Trace.error("RM die after deciding but before sending answer");
                    System.exit(0);
                }


                return true;
            } catch (IOException e) {
                TCPServer.diskOperator.writeLogRecord("ABORT");
                //@crash
                if (RMDieAfterDecision) {
                    Trace.error("RM die after deciding but before sending answer");
                    System.exit(0);
                }
                return false;
            }
        } else if (TCPServer.serverType.equals("CAR_RM")) {
            try {
                TCPServer.diskOperator.writeDataToDisk(TCPServer.m_itemHT_car, "car" + shadowVersion);
                TCPServer.diskOperator.writeLogRecord("YES,");


                //@crash
                if (RMDieAfterDecision) {
                    Trace.error("RM die after deciding but before sending answer");
                    System.exit(0);
                }

                return true;
            } catch (IOException e) {
                TCPServer.diskOperator.writeLogRecord("ABORT");


                //@crash
                if (RMDieAfterDecision) {
                    Trace.error("RM die after deciding but before sending answer");
                    System.exit(0);
                }

                return false;
            }
        } else if (TCPServer.serverType.equals("ROOM_RM")) {
            try {
                TCPServer.diskOperator.writeDataToDisk(TCPServer.m_itemHT_room, "room" + shadowVersion);
                TCPServer.diskOperator.writeLogRecord("YES,");

                //@crash
                if (RMDieAfterDecision) {
                    Trace.error("RM die after deciding but before sending answer");
                    System.exit(0);
                }

                return true;
            } catch (IOException e) {
                TCPServer.diskOperator.writeLogRecord("ABORT");

                //@crash
                if (RMDieAfterDecision) {
                    Trace.error("RM die after deciding but before sending answer");
                    System.exit(0);
                }

                return false;
            }
        }
        TCPServer.diskOperator.writeLogRecord("ABORT");
        return false;
    }

    private int findChoice(String[] cmdWords) {
        int choice = -1;

        if (cmdWords[0].compareToIgnoreCase("help") == 0)
            choice = 1;
        else if (cmdWords[0].compareToIgnoreCase("addflight") == 0)
            choice = 2;
        else if (cmdWords[0].compareToIgnoreCase("addcars") == 0)
            choice = 3;
        else if (cmdWords[0].compareToIgnoreCase("addrooms") == 0)
            choice = 4;
        else if (cmdWords[0].compareToIgnoreCase("newcustomer") == 0)
            choice = 5;
        else if (cmdWords[0].compareToIgnoreCase("deleteflight") == 0)
            choice = 6;
        else if (cmdWords[0].compareToIgnoreCase("deletecars") == 0)
            choice = 7;
        else if (cmdWords[0].compareToIgnoreCase("deleterooms") == 0)
            choice = 8;
        else if (cmdWords[0].compareToIgnoreCase("deletecustomer") == 0)
            choice = 9;
        else if (cmdWords[0].compareToIgnoreCase("queryflight") == 0)
            choice = 10;
        else if (cmdWords[0].compareToIgnoreCase("querycars") == 0)
            choice = 11;
        else if (cmdWords[0].compareToIgnoreCase("queryrooms") == 0)
            choice = 12;
        else if (cmdWords[0].compareToIgnoreCase("querycustomerinfo") == 0)
            choice = 13;
        else if (cmdWords[0].compareToIgnoreCase("queryflightprice") == 0)
            choice = 14;
        else if (cmdWords[0].compareToIgnoreCase("querycarsprice") == 0)
            choice = 15;
        else if (cmdWords[0].compareToIgnoreCase("queryroomsprice") == 0)
            choice = 16;
        else if (cmdWords[0].compareToIgnoreCase("reserveflight") == 0)
            choice = 17;
        else if (cmdWords[0].compareToIgnoreCase("reservecar") == 0)
            choice = 18;
        else if (cmdWords[0].compareToIgnoreCase("reserveroom") == 0)
            choice = 19;
        else if (cmdWords[0].compareToIgnoreCase("reserveitinerary") == 0)
            choice = 20;
        else if (cmdWords[0].compareToIgnoreCase("END") == 0)
            choice = 21;
        else if (cmdWords[0].compareToIgnoreCase("newcustomerid") == 0)
            choice = 22;
        else if (cmdWords[0].compareToIgnoreCase("increasereservableitemcount") == 0)
            choice = 23;
        else if (cmdWords[0].compareToIgnoreCase("undoaddflight") == 0)
            choice = 24;
        else if (cmdWords[0].compareToIgnoreCase("undoaddcars") == 0)
            choice = 25;
        else if (cmdWords[0].compareToIgnoreCase("undoaddrooms") == 0)
            choice = 26;
        else if (cmdWords[0].compareToIgnoreCase("queryflightreserved") == 0)
            choice = 27;
        else if (cmdWords[0].compareToIgnoreCase("querycarsreserved") == 0)
            choice = 28;
        else if (cmdWords[0].compareToIgnoreCase("queryroomsreserved") == 0)
            choice = 29;
        else if (cmdWords[0].compareToIgnoreCase("isexistingflight") == 0)
            choice = 30;
        else if (cmdWords[0].compareToIgnoreCase("isexistingcars") == 0)
            choice = 31;
        else if (cmdWords[0].compareToIgnoreCase("isexistingrooms") == 0)
            choice = 32;
        else if (cmdWords[0].compareToIgnoreCase("decreasereservableitemcount") == 0)
            choice = 33;
        else if (cmdWords[0].compareToIgnoreCase("aborta") == 0)
            choice = 63;
        else if (cmdWords[0].compareToIgnoreCase("abortb") == 0)
            choice = 63;
        else if (cmdWords[0].compareToIgnoreCase("writea") == 0)
            choice = 64;
        else if (cmdWords[0].compareToIgnoreCase("writeb") == 0)
            choice = 64;
        else if (cmdWords[0].compareToIgnoreCase("writecompletedata") == 0)
            choice = 65;
        else if (cmdWords[0].compareToIgnoreCase("shutdown") == 0)
            choice = 66;
        else if (cmdWords[0].compareToIgnoreCase("destruct") == 0)
            choice = 66;
        else if (cmdWords[0].compareToIgnoreCase("commita") == 0)
            choice = 67;
        else if (cmdWords[0].compareToIgnoreCase("commitb") == 0)
            choice = 67;
        else if (cmdWords[0].compareToIgnoreCase("loada") == 0)
            choice  = 68;
        else if (cmdWords[0].compareToIgnoreCase("loadb") == 0)
            choice = 68;
        else if (cmdWords[0].contains("rmdie"))
            choice = 69;
        else
            choice = -1;
        return choice;
    }

    private void writeCompleteData(int id, String key,
                                   int numItem, int price, int numReserved) {

        //@crash
        if (RMDieAfterRequest) {
            Trace.error("RM die after receiving vote request but before preparing");
            System.exit(0);
        }

        if (numItem == -1) {
            removeData(id, key);
        } else if (key.contains(TransactionManager.FLIGHT)) {
            Flight newObj = new Flight(Integer.parseInt(key.replace(TransactionManager.FLIGHT, "")), numItem, price);
            newObj.setReserved(numReserved);
            writeData(id, key, newObj);
        } else if (key.contains(TransactionManager.CAR)) {
            Car newObj = new Car(key.replace(TransactionManager.CAR, ""), numItem, price);
            newObj.setReserved(numReserved);
            writeData(id, key, newObj);
        } else if (key.contains(TransactionManager.ROOM)) {
            Room newObj = new Room(key.replace(TransactionManager.CAR, ""), numItem, price);
            newObj.setReserved(numReserved);
            writeData(id, key, newObj);
        }
        Trace.info("data merged: " + key + "," + numItem + "," + price + "," + numReserved);
    }

    // Basic operations on ResourceManager.RMItem //

    // Read a data item.
    public RMItem readData(int id, String key) {
        RMHashtable hashTable;
        if (TCPServer.serverType.equals(TCPServer.FLIGHT_RM)) {
            hashTable = TCPServer.m_itemHT_flight;
        } else if (TCPServer.serverType.equals(TCPServer.CAR_RM)) {
            hashTable = TCPServer.m_itemHT_car;
        } else
            hashTable = TCPServer.m_itemHT_room;
        synchronized (hashTable) {
            return (RMItem) hashTable.get(key);
        }
    }

    // Write a data item.
    public void writeData(int id, String key, RMItem value) {
        RMHashtable hashTable;
        if (TCPServer.serverType.equals(TCPServer.FLIGHT_RM)) {
            hashTable = TCPServer.m_itemHT_flight;
        } else if (TCPServer.serverType.equals(TCPServer.CAR_RM)) {
            hashTable = TCPServer.m_itemHT_car;
        } else
            hashTable = TCPServer.m_itemHT_room;
        synchronized (hashTable) {
            hashTable.put(key, value);
        }
    }

    // Remove the item out of storage.
    public RMItem removeData(int id, String key) {
        RMHashtable hashTable;
        if (TCPServer.serverType.equals(TCPServer.FLIGHT_RM)) {
            hashTable = TCPServer.m_itemHT_flight;
        } else if (TCPServer.serverType.equals(TCPServer.CAR_RM)) {
            hashTable = TCPServer.m_itemHT_car;
        } else
            hashTable = TCPServer.m_itemHT_room;
        synchronized (hashTable) {
            return (RMItem) hashTable.remove(key);
        }
    }


    // Basic operations on ResourceManager.ReservableItem //

    // Delete the entire item.
    protected boolean deleteItem(int id, String key) {
        Trace.info("RM::deleteItem(" + id + ", " + key + ") called.");
        ReservableItem curObj = (ReservableItem) readData(id, key);
        // Check if there is such an item in the storage.
        if (curObj == null) {
            Trace.warn("RM::deleteItem(" + id + ", " + key + ") failed: "
                    + " item doesn't exist.");
            return false;
        } else {
            if (curObj.getReserved() == 0) {
                removeData(id, curObj.getKey());
                Trace.info("RM::deleteItem(" + id + ", " + key + ") OK.");
                return true;
            } else {
                Trace.info("RM::deleteItem(" + id + ", " + key + ") failed: "
                        + "some customers have reserved it.");
                return false;
            }
        }
    }

    // Query the number of available seats/rooms/cars.
    protected int queryNum(int id, String key) {
        Trace.info("RM::queryNum(" + id + ", " + key + ") called.");
        ReservableItem curObj = (ReservableItem) readData(id, key);
        int value = -1;
        if (curObj != null) {
            value = curObj.getCount();
        }
        Trace.info("RM::queryNum(" + id + ", " + key + ") OK: " + value);
        return value;
    }

    // Query the price of an item.
    protected int queryPrice(int id, String key) {
        Trace.info("RM::queryPrice(" + id + ", " + key + ") called.");
        ReservableItem curObj = (ReservableItem) readData(id, key);
        int value = -1;
        if (curObj != null) {
            value = curObj.getPrice();
        }
        Trace.info("RM::queryPrice(" + id + ", " + key + ") OK: $" + value);
        return value;
    }

    // update count and reserved
    protected boolean reserveItem(int id, int customerId,
                                  String key, String location) {
        Trace.info("RM::reserveItem(" + id + ", " + customerId + ", "
                + key + ", " + location + ") called.");
        // Check if the item is available.
        ReservableItem item = (ReservableItem) readData(id, key);
        if (item == null) {
            Trace.warn("RM::reserveItem(" + id + ", " + customerId + ", "
                    + key + ", " + location + ") failed: item doesn't exist.");
            return false;
        } else if (item.getCount() == 0) {
            Trace.warn("RM::reserveItem(" + id + ", " + customerId + ", "
                    + key + ", " + location + ") failed: no more items.");
            return false;
        } else {
            // Decrease the number of available items in the storage.
            item.setCount(item.getCount() - 1);
            item.setReserved(item.getReserved() + 1);

            Trace.warn("RM::reserveItem(" + id + ", " + customerId + ", "
                    + key + ", " + location + ") OK.");
            return true;
        }
    }

    // ResourceManager.Flight operations //

    public boolean isExistingFlight(int id, int flightNumber) {
        Trace.info("RM::isExistingFlight(" + id + ", " + flightNumber + ")");
        Flight curObj = (Flight) readData(id, Flight.getKey(flightNumber));
        if (curObj == null) {
            return false;
        } else return true;
    }

    // Create a new flight, or add seats to existing flight.
    // Note: if flightPrice <= 0 and the flight already exists, it maintains
    // its current price.
    @Override
    public boolean addFlight(int id, int flightNumber,
                             int numSeats, int flightPrice) {
        Trace.info("RM::addFlight(" + id + ", " + flightNumber
                + ", $" + flightPrice + ", " + numSeats + ") called.");
        Flight curObj = (Flight) readData(id, Flight.getKey(flightNumber));
        if (curObj == null) {
            // Doesn't exist; add it.
            Flight newObj = new Flight(flightNumber, numSeats, flightPrice);
            writeData(id, newObj.getKey(), newObj);
            Trace.info("RM::addFlight(" + id + ", " + flightNumber
                    + ", $" + flightPrice + ", " + numSeats + ") OK.");
        } else {
            // Add seats to existing flight and update the price.
            curObj.setCount(curObj.getCount() + numSeats);
            if (flightPrice > 0) {
                curObj.setPrice(flightPrice);
            }
            //the line below should be redundant
            writeData(id, curObj.getKey(), curObj);
            Trace.info("RM::addFlight(" + id + ", " + flightNumber
                    + ", $" + flightPrice + ", " + numSeats + ") OK: "
                    + "seats = " + curObj.getCount() + ", price = $" + flightPrice);
        }
        return (true);
    }

    private boolean undoAddFlight(int id, int flightNumber, int numSeats, int flightPrice, int numReserved) {
        Trace.info("RM::undoAddFlight(" + id + ", " + flightNumber
                + ", $" + flightPrice + ", " + numSeats + ") called.");
        Flight curObj = (Flight) readData(id, Flight.getKey(flightNumber));
        if (curObj == null) {
            // Doesn't exist; add it.
            Flight newObj = new Flight(flightNumber, numSeats, flightPrice);
            writeData(id, newObj.getKey(), newObj);
        } else {
            curObj.setCount(numSeats);
            curObj.setReserved(numReserved);
            if (flightPrice > 0) {
                curObj.setPrice(flightPrice);
            }
            //the line below should be redundant
            writeData(id, curObj.getKey(), curObj);
        }
        return (true);
    }

    @Override
    public boolean deleteFlight(int id, int flightNumber) {
        return deleteItem(id, Flight.getKey(flightNumber));
    }

    // Returns the number of empty seats on this flight.
    @Override
    public int queryFlight(int id, int flightNumber) {
        return queryNum(id, Flight.getKey(flightNumber));
    }

    // Returns price of this flight.
    public int queryFlightPrice(int id, int flightNumber) {
        return queryPrice(id, Flight.getKey(flightNumber));
    }

    public int queryFlightReserved(int id, int flightNumber) {
        ReservableItem curObj = (ReservableItem) readData(id, Flight.getKey(flightNumber));
        int value = 0;
        if (curObj != null) {
            value = curObj.getReserved();
        }
        return value;
    }

    /*
    // Returns the number of reservations for this flight.
    public int queryFlightReservations(int id, int flightNumber) {
        ResourceManager.Trace.info("RM::queryFlightReservations(" + id
                + ", #" + flightNumber + ") called.");
        ResourceManager.RMInteger numReservations = (ResourceManager.RMInteger) readData(id,
                ResourceManager.Flight.getNumReservationsKey(flightNumber));
        if (numReservations == null) {
            numReservations = new ResourceManager.RMInteger(0);
       }
        ResourceManager.Trace.info("RM::queryFlightReservations(" + id +
                ", #" + flightNumber + ") = " + numReservations);
        return numReservations.getValue();
    }
    */

    /*
    // Frees flight reservation record. ResourceManager.Flight reservation records help us
    // make sure we don't delete a flight if one or more customers are
    // holding reservations.
    public boolean freeFlightReservation(int id, int flightNumber) {
        ResourceManager.Trace.info("RM::freeFlightReservations(" + id + ", "
                + flightNumber + ") called.");
        ResourceManager.RMInteger numReservations = (ResourceManager.RMInteger) readData(id,
                ResourceManager.Flight.getNumReservationsKey(flightNumber));
        if (numReservations != null) {
            numReservations = new ResourceManager.RMInteger(
                    Math.max(0, numReservations.getValue() - 1));
        }
        writeData(id, ResourceManager.Flight.getNumReservationsKey(flightNumber), numReservations);
        ResourceManager.Trace.info("RM::freeFlightReservations(" + id + ", "
                + flightNumber + ") OK: reservations = " + numReservations);
        return true;
    }
    */


    // ResourceManager.Car operations //

    public boolean isExistingCars(int id, String location) {
        Trace.info("RM::isExistingCars(" + id + ", " + location + ")");
        Car curObj = (Car) readData(id, Car.getKey(location));
        if (curObj == null) {
            return false;
        } else return true;
    }

    // Create a new car location or add cars to an existing location.
    // Note: if price <= 0 and the car location already exists, it maintains
    // its current price.
    @Override
    public boolean addCars(int id, String location, int numCars, int carPrice) {
        Trace.info("RM::addCars(" + id + ", " + location + ", "
                + numCars + ", $" + carPrice + ") called.");
        Car curObj = (Car) readData(id, Car.getKey(location));
        if (curObj == null) {
            // Doesn't exist; add it.
            Car newObj = new Car(location, numCars, carPrice);
            writeData(id, newObj.getKey(), newObj);
            Trace.info("RM::addCars(" + id + ", " + location + ", "
                    + numCars + ", $" + carPrice + ") OK.");
        } else {
            // Add count to existing object and update price.
            curObj.setCount(curObj.getCount() + numCars);
            if (carPrice > 0) {
                curObj.setPrice(carPrice);
            }
            writeData(id, curObj.getKey(), curObj);
            Trace.info("RM::addCars(" + id + ", " + location + ", "
                    + numCars + ", $" + carPrice + ") OK: "
                    + "cars = " + curObj.getCount() + ", price = $" + carPrice);
        }
        return true;
    }

    private boolean undoAddCars(int id, String location, int numCars, int carPrice, int numReserved) {
        Trace.info("RM::undoAddCars(" + id + ", " + location
                + ", $" + carPrice + ", " + numCars + ") called.");
        Car curObj = (Car) readData(id, Car.getKey(location));
        if (curObj == null) {
            // Doesn't exist; add it.
            Car newObj = new Car(location, numCars, carPrice);
            writeData(id, newObj.getKey(), newObj);
        } else {
            curObj.setCount(numCars);
            curObj.setReserved(numReserved);
            if (carPrice > 0) {
                curObj.setPrice(carPrice);
            }
            //the line below should be redundant
            writeData(id, curObj.getKey(), curObj);
        }
        return (true);
    }

    // Delete cars from a location.
    @Override
    public boolean deleteCars(int id, String location) {
        return deleteItem(id, Car.getKey(location));
    }

    // Returns the number of cars available at a location.
    @Override
    public int queryCars(int id, String location) {
        return queryNum(id, Car.getKey(location));
    }

    // Returns price of cars at this location.
    @Override
    public int queryCarsPrice(int id, String location) {
        return queryPrice(id, Car.getKey(location));
    }

    public int queryCarsReserved(int id, String location) {
        ReservableItem curObj = (ReservableItem) readData(id, Car.getKey(location));
        int value = 0;
        if (curObj != null) {
            value = curObj.getReserved();
        }
        return value;
    }


    // ResourceManager.Room operations //

    public boolean isExistingRooms(int id, String location) {
        Trace.info("RM::isExistingRooms(" + id + ", " + location + ")");
        Room curObj = (Room) readData(id, Room.getKey(location));
        if (curObj == null) {
            return false;
        } else return true;
    }

    // Create a new room location or add rooms to an existing location.
    // Note: if price <= 0 and the room location already exists, it maintains
    // its current price.
    @Override
    public boolean addRooms(int id, String location, int numRooms, int roomPrice) {
        Trace.info("RM::addRooms(" + id + ", " + location + ", "
                + numRooms + ", $" + roomPrice + ") called.");
        Room curObj = (Room) readData(id, Room.getKey(location));
        if (curObj == null) {
            // Doesn't exist; add it.
            Room newObj = new Room(location, numRooms, roomPrice);
            writeData(id, newObj.getKey(), newObj);
            Trace.info("RM::addRooms(" + id + ", " + location + ", "
                    + numRooms + ", $" + roomPrice + ") OK.");
        } else {
            // Add count to existing object and update price.
            curObj.setCount(curObj.getCount() + numRooms);
            if (roomPrice > 0) {
                curObj.setPrice(roomPrice);
            }
            writeData(id, curObj.getKey(), curObj);
            Trace.info("RM::addRooms(" + id + ", " + location + ", "
                    + numRooms + ", $" + roomPrice + ") OK: "
                    + "rooms = " + curObj.getCount() + ", price = $" + roomPrice);
        }
        return true;
    }

    private boolean undoAddRooms(int id, String location, int numRooms, int roomPrice, int numReserved) {
        Trace.info("RM::undoAddRooms(" + id + ", " + location
                + ", $" + roomPrice + ", " + numRooms + ") called.");
        Room curObj = (Room) readData(id, Room.getKey(location));
        if (curObj == null) {
            // Doesn't exist; add it.
            Room newObj = new Room(location, numRooms, roomPrice);
            writeData(id, newObj.getKey(), newObj);
        } else {
            curObj.setCount(numRooms);
            curObj.setReserved(numReserved);
            if (roomPrice > 0) {
                curObj.setPrice(roomPrice);
            }
            //the line below should be redundant
            writeData(id, curObj.getKey(), curObj);
        }
        return (true);
    }


    // Delete rooms from a location.
    @Override
    public boolean deleteRooms(int id, String location) {
        return deleteItem(id, Room.getKey(location));
    }

    // Returns the number of rooms available at a location.
    @Override
    public int queryRooms(int id, String location) {
        return queryNum(id, Room.getKey(location));
    }

    // Returns room price at this location.
    @Override
    public int queryRoomsPrice(int id, String location) {
        return queryPrice(id, Room.getKey(location));
    }

    public int queryRoomsReserved(int id, String location) {
        ReservableItem curObj = (ReservableItem) readData(id, Room.getKey(location));
        int value = 0;
        if (curObj != null) {
            value = curObj.getReserved();
        }
        return value;
    }


    // ResourceManager.Customer operations //

    @Override
    public int newCustomer(int id) {
        Trace.info("INFO: RM::newCustomer(" + id + ") called.");
        // Generate a globally unique Id for the new customer.
        int customerId = Integer.parseInt(String.valueOf(id) +
                String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
                String.valueOf(Math.round(Math.random() * 100 + 1)));
        Customer cust = new Customer(customerId);
        writeData(id, cust.getKey(), cust);
        Trace.info("RM::newCustomer(" + id + ") OK: " + customerId);
        return customerId;
    }

    // This method makes testing easier.
    @Override
    public boolean newCustomerId(int id, int customerId) {
        Trace.info("INFO: RM::newCustomer(" + id + ", " + customerId + ") called.");
        Customer cust = (Customer) readData(id, Customer.getKey(customerId));
        if (cust == null) {
            cust = new Customer(customerId);
            writeData(id, cust.getKey(), cust);
            Trace.info("INFO: RM::newCustomer(" + id + ", " + customerId + ") OK.");
            return true;
        } else {
            Trace.info("INFO: RM::newCustomer(" + id + ", " +
                    customerId + ") failed: customer already exists.");
            return false;
        }
    }

    // Delete customer from the database.
    @Override
    public boolean deleteCustomer(int id, int customerId) {
        Trace.info("RM::deleteCustomer(" + id + ", " + customerId + ") called.");
        Customer cust = (Customer) readData(id, Customer.getKey(customerId));
        if (cust == null) {
            Trace.warn("RM::deleteCustomer(" + id + ", "
                    + customerId + ") failed: customer doesn't exist.");
            return false;
        } else {
            // Increase the reserved numbers of all reservable items that
            // the customer reserved.
            RMHashtable reservationHT = cust.getReservations();
            for (Enumeration e = reservationHT.keys(); e.hasMoreElements(); ) {
                String reservedKey = (String) (e.nextElement());
                ReservedItem reservedItem = cust.getReservedItem(reservedKey);
                Trace.info("RM::deleteCustomer(" + id + ", " + customerId + "): "
                        + "deleting " + reservedItem.getCount() + " reservations "
                        + "for item " + reservedItem.getKey());
                ReservableItem item =
                        (ReservableItem) readData(id, reservedItem.getKey());
                item.setReserved(item.getReserved() - reservedItem.getCount());
                item.setCount(item.getCount() + reservedItem.getCount());
                Trace.info("RM::deleteCustomer(" + id + ", " + customerId + "): "
                        + reservedItem.getKey() + " reserved/available = "
                        + item.getReserved() + "/" + item.getCount());
            }
            // Remove the customer from the storage.
            removeData(id, cust.getKey());
            Trace.info("RM::deleteCustomer(" + id + ", " + customerId + ") OK.");
            return true;
        }
    }

    // Return data structure containing customer reservation info.
    // Returns null if the customer doesn't exist.
    // Returns empty ResourceManager.RMHashtable if customer exists but has no reservations.
    public RMHashtable getCustomerReservations(int id, int customerId) {
        Trace.info("RM::getCustomerReservations(" + id + ", "
                + customerId + ") called.");
        Customer cust = (Customer) readData(id, Customer.getKey(customerId));
        if (cust == null) {
            Trace.info("RM::getCustomerReservations(" + id + ", "
                    + customerId + ") failed: customer doesn't exist.");
            return null;
        } else {
            return cust.getReservations();
        }
    }

    // Return a bill.
    @Override
    public String queryCustomerInfo(int id, int customerId) {
        Trace.info("RM::queryCustomerInfo(" + id + ", " + customerId + ") called.");
        Customer cust = (Customer) readData(id, Customer.getKey(customerId));
        if (cust == null) {
            Trace.warn("RM::queryCustomerInfo(" + id + ", "
                    + customerId + ") failed: customer doesn't exist.");
            // Returning an empty bill means that the customer doesn't exist.
            return "";
        } else {
            String s = cust.printBill();
            Trace.info("RM::queryCustomerInfo(" + id + ", " + customerId + "): \n");
            System.out.println(s);
            return s;
        }
    }

    // Add flight reservation to this customer.
    @Override
    public boolean reserveFlight(int id, int customerId, int flightNumber) {
        return reserveItem(id, customerId,
                Flight.getKey(flightNumber), String.valueOf(flightNumber));
    }

    // Add car reservation to this customer.
    @Override
    public boolean reserveCar(int id, int customerId, String location) {
        return reserveItem(id, customerId, Car.getKey(location), location);
    }

    // Add room reservation to this customer.
    @Override
    public boolean reserveRoom(int id, int customerId, String location) {
        return reserveItem(id, customerId, Room.getKey(location), location);
    }


    // Reserve an itinerary.
    @Override
    public boolean reserveItinerary(int id, int customerId, Vector flightNumbers,
                                    String location, boolean car, boolean room) {
        return false;
    }

    @Override
    public boolean increaseReservableItemCount(int id, String key, int count) {
        ReservableItem item =
                (ReservableItem) readData(id, key);
        if (item == null) {
            Trace.info("no such item, cannot increase count");
            return false;
        }
        item.setReserved(item.getReserved() - count);
        item.setCount(item.getCount() + count);
        Trace.info("item reserved: " + item.getReserved() + "    item count: " + item.getCount());

        return true;
    }

    public boolean decreaseReservableItemCount(int id, String key, int count) {
        ReservableItem item =
                (ReservableItem) readData(id, key);
        if (item == null) {
            Trace.info("no such item, cannot increase count");
            return false;
        }
        item.setReserved(item.getReserved() + count);
        item.setCount(item.getCount() - count);
        Trace.info("item reserved: " + item.getReserved() + "    item count: " + item.getCount());

        return true;
    }

}
