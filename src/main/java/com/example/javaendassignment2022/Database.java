package com.example.javaendassignment2022;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Member> members = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public Database() {
        users.add(new User("Admin", "Admin"));
        users.add(new User("Jun", "Jun"));

        members.add(new Member(1,  "Jun", "Feng", LocalDate.of(2003,2,16)));

        items.add(new Item(1, true, "Of Mice And Men", "John Steinbeck", null, null));
        items.add(new Item(2, false, "Animal Farm", "John Steinbeck", members.get(0), LocalDate.of(2022, 10, 1)));
    }


    public List<User> getUsers() {
        return users;
    }
    public List<Member> getMembers() {
        return members;
    }

    private int idIncrementing(List list) {
        if(list.isEmpty()) {
            return 1;
        }

        if (list.get(0).getClass() == Member.class) {
            return ((Member)list.get(list.size() - 1)).getId() + 1;
        }
        else
            return ((Item)list.get(list.size() - 1)).getItemCode() + 1;
    }

    public void addMember(String firstName, String lastName, LocalDate localDate) {

        members.add(new Member(idIncrementing(members), firstName, lastName, localDate ));
    }
    public void removeMember(Member member) {
        members.remove(member);
    }

    public void editMembers(Member member, String firstName, String lastName, LocalDate localDate ) {
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setBirthDate(localDate);
    }

    public List<Item> getItems() {
        return items;
    }
    public void addItems(Boolean availability, String title, String author, Member member, LocalDate lendDate) {
        items.add(new Item(idIncrementing(items), availability, title, author, member, lendDate));
    }

    public void editItem(Item item, Boolean availability, String title, String author) {
        item.setAvailability(availability);
        item.setTitle(title);
        item.setAuthor(author);
    }

    public void lendItem(Item item, Member member) {
        item.setLendDate(LocalDate.now());
        item.setAvailability(false);
        item.setMember(member);
    }

    public void receiveItem(Item item) {
        item.setLendDate(null);
        item.setAvailability(true);
        item.setMember(null);
    }
    public void removeItem(Item item) {
        items.remove(item);
    }

    public void saveItems() {
        //Save items to disk
        try (FileOutputStream fos = new FileOutputStream(new File("items.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Item item : items) {
                oos.writeObject(item);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMembers() {
        //Save items to disk
        try (FileOutputStream fos = new FileOutputStream(new File("members.dat"));
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Member member : members) {
                oos.writeObject(member);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadItems() {
        //Load items from disk
        items.clear();

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("items.dat")))) {
            while (true) {
                try {
                    Item item = (Item) ois.readObject();
                    items.add(item);
                } catch (EOFException eof) {
                    break;
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMembers() {
        //Load members from disk
        members.clear();

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("members.dat")))) {
            while (true) {
                try {
                    Member member = (Member) ois.readObject();
                    members.add(member);
                } catch (EOFException eof) {
                    break;
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkFileAvailability(File file) {
        if(file.exists() && !file.isDirectory()) {
            if (file.getName().equals("items.dat")){
                loadItems();
            }
            else
                loadMembers();
        }
        else {
            if (file.getName().equals("items.dat")) {
                saveItems();
                loadItems();
            }
            else {
                saveMembers();
                loadMembers();
            }
        }
    }
}
