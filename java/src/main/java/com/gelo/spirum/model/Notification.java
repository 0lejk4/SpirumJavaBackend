package com.gelo.spirum.model;

public class Notification
{
    public Notification()
    {
    }

    public Notification(String type, String text, String title)
    {
        this.type = type;
        this.text = text;
        this.title = title;
    }

    private String type;
    private String text;
    private String title;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
