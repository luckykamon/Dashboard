package services.weather;

public class widgetWeather {

    public static void main(String[] args) {
        System.out.println("Widget Global: " + widgetWeather.iFrameWidgetWeather(true, true, 7, "Rennes"));
    }

    public static String iFrameWidgetWeather(boolean temperature, boolean pictoIcon, int nbDay, String ville) {
        int pictoicon = 0;
        if (pictoIcon) {
            pictoicon = 1;
        }
        int temperatureNb = 0;
        if (temperature) {
            temperatureNb = 1;
        }
        String villeCode = switch (ville) {
            case "Rennes" -> "/rennes_france_2983990?geoloc=fixed";
            case "Marseille" -> "/marseille_france_2995469?geoloc=fixed";
            case "Lyon" -> "/lyon_france_2996944?geoloc=fixed";
            case "Toulouse" -> "/toulouse_france_2972315?geoloc=fixed";
            case "Nice" -> "/nice_france_2990440?geoloc=fixed";
            case "Nantes" -> "/nantes_france_2990969?geoloc=fixed";
            case "Montpellier" -> "/montpellier_france_2992166?geoloc=fixed";
            case "Paris" -> "/paris_france_2988507?geoloc=fixed";
            case "Strasbourg" -> "/strasbourg_france_2973783?geoloc=fixed";
            case "Bordeaux" -> "/bordeaux_france_3031582?geoloc=fixed";
            case "Lille" -> "/lille_france_2998324?geoloc=fixed";
            case "Brest" -> "/brest_france_3030300?geoloc=fixed";
            default -> "?geoloc=detect";
        };
        return "<iframe src=\"https://www.meteoblue.com/fr/meteo/widget/daily" + villeCode + "&days=" + nbDay + "&tempunit=" + "CELSIUS" + "&windunit=KILOMETER_PER_HOUR&precipunit=MILLIMETER&coloured=coloured&pictoicon=" + pictoicon + "&maxtemperature=" + temperatureNb + "&mintemperature=" + temperatureNb + "&windspeed=0&windgust=0&winddirection=0&uv=0&humidity=0&precipitation=0&precipitationprobability=0&spot=0&pressure=0&layout=light\"\n" +
                "                scrolling=\"NO\" allowtransparency=\"true\"\n" +
                "                sandbox=\"allow-same-origin allow-scripts allow-popups allow-popups-to-escape-sandbox\"\n" +
                "                style=\"width: 100%; height: 100%; border: 0\"></iframe>";
    }


}
