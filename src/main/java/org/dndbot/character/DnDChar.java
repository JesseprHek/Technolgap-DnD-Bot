package org.dndbot.character;

public class DnDChar {
    private String name;
    private String DnDClass;
    private long UserId;

    private int strength, dexterity, constitution, intelligence, wisdom, charisma;

    public DnDChar() {}

    public DnDChar(String name, String DnDClass, long DiscordID) {
        this.name = name;
        this.DnDClass = DnDClass;
        this.UserId = DiscordID;
        // Default stats
        strength = 10;
        dexterity = 10;
        constitution = 10;
        intelligence = 10;
        wisdom = 10;
        charisma = 10;
    }

    public DnDChar(String name, String DnDClass, long UserId, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.name = name;
        this.DnDClass = DnDClass;
        this.UserId = UserId;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
    }

    public String getName() {
        return name;
    }

    public String getDnDClass() {
        return DnDClass;
    }

    public long getUserId() {
        return UserId;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDnDClass(String DnDClass) {
        this.DnDClass = DnDClass;
    }

    public void setUserId(long UserId) {
        this.UserId = UserId;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
}
