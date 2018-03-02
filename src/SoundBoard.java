import java.io.File;

class SoundBoard {

    public static void main(String[] args) {

        File file = new File("sounds/");

        if (!file.exists())
            if (file.mkdir())
                System.out.println("Sounds directory has been created !");

        new Window();
    }
}
