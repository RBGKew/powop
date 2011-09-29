package org.emonocot.model.geography;

/**
 *
 * @author ben
 *
 */
public enum Country implements GeographicalRegion<Country> {
    DEN("DEN", "Denmark", Region.NORTHERN_EUROPE, "DK"),
    FIN("FIN", "Finland", Region.NORTHERN_EUROPE, "FI"),
    FOR("FOR", "Føroyar", Region.NORTHERN_EUROPE, "FO"),
    GRB("GRB", "Great Britain", Region.NORTHERN_EUROPE, "UK"),
    ICE("ICE", "Iceland", Region.NORTHERN_EUROPE, "IS"),
    IRE("IRE", "Ireland", Region.NORTHERN_EUROPE),
    NOR("NOR", "Norway", Region.NORTHERN_EUROPE, "NO"),
    SVA("SVA", "Svalbard", Region.NORTHERN_EUROPE, "SJ"),
    SWE("SWE", "Sweden", Region.NORTHERN_EUROPE, "SE"),
    AUT("AUT", "Austria", Region.MIDDLE_EUROPE),
    BGM("BGM", "Belgium", Region.MIDDLE_EUROPE),
    CZE("CZE", "Czechoslovakia", Region.MIDDLE_EUROPE, "CS"),
    GER("GER", "Germany", Region.MIDDLE_EUROPE, "DE"),
    HUN("HUN", "Hungary", Region.MIDDLE_EUROPE, "HU"),
    NET("NET", "Netherlands", Region.MIDDLE_EUROPE, "NL"),
    POL("POL", "Poland", Region.MIDDLE_EUROPE, "PL"),
    SWI("SWI", "Switzerland", Region.MIDDLE_EUROPE, "CH"),
    BAL("BAL", "Baleares", Region.SOUTHWESTERN_EUROPE, "ES"),
    COR("COR", "Corse", Region.SOUTHWESTERN_EUROPE, "FR"),
    FRA("FRA", "France", Region.SOUTHWESTERN_EUROPE),
    POR("POR", "Portugal", Region.SOUTHWESTERN_EUROPE, "PT"),
    SAR("SAR", "Sardegna", Region.SOUTHWESTERN_EUROPE, "IT"),
    SPA("SPA", "Spain", Region.SOUTHWESTERN_EUROPE),
    ALB("ALB", "Albania", Region.SOUTHEASTERN_EUROPE, "AL"),
    BUL("BUL", "Bulgaria", Region.SOUTHEASTERN_EUROPE, "BG"),
    GRC("GRC", "Greece", Region.SOUTHEASTERN_EUROPE, "GR"),
    ITA("ITA", "Italy", Region.SOUTHEASTERN_EUROPE),
    KRI("KRI", "Kriti", Region.SOUTHEASTERN_EUROPE, "GR"),
    ROM("ROM", "Romania", Region.SOUTHEASTERN_EUROPE, "RO"),
    SIC("SIC", "Sicilia", Region.SOUTHEASTERN_EUROPE),
    TUE("TUE", "Turkey-in-Europe", Region.SOUTHEASTERN_EUROPE, "TR"),
    YUG("YUG", "Yugoslavia", Region.SOUTHEASTERN_EUROPE, "YU"),
    BLR("BLR", "Belarus", Region.EASTERN_EUROPE, "RU"),
    BLT("BLT", "Baltic States", Region.EASTERN_EUROPE),
    KRY("KRY", "Krym", Region.EASTERN_EUROPE, "UA"),
    RUC("RUC", "Central European Russia", Region.EASTERN_EUROPE, "RU"),
    RUE("RUE", "East European Russia", Region.EASTERN_EUROPE, "RU"),
    RUN("RUN", "North European Russia", Region.EASTERN_EUROPE, "RU"),
    RUS("RUS", "South European Russia", Region.EASTERN_EUROPE),
    RUW("RUW", "Northwest European Russia", Region.EASTERN_EUROPE, "RU"),
    UKR("UKR", "Ukraine", Region.EASTERN_EUROPE),
    ALG("ALG", "Algeria", Region.NORTHERN_AFRICA, "DZ"),
    EGY("EGY", "Egypt", Region.NORTHERN_AFRICA, "EG"),
    LBY("LBY", "Libya", Region.NORTHERN_AFRICA, "LY"),
    MOR("MOR", "Morocco", Region.NORTHERN_AFRICA),
    TUN("TUN", "Tunisia", Region.NORTHERN_AFRICA, "TN"),
    WSA("WSA", "Western Sahara", Region.NORTHERN_AFRICA, "EH"),
    AZO("AZO", "Azores", Region.MACRONESIA, "PT"),
    CNY("CNY", "Canary Is.", Region.MACRONESIA, "ES"),
    CVI("CVI", "Cape Verde", Region.MACRONESIA, "CV"),
    MDR("MDR", "Madeira", Region.MACRONESIA, "PT"),
    SEL("SEL", "Selvagens", Region.MACRONESIA, "PT"),
    BEN("BEN", "Benin", Region.WEST_TROPICAL_AFRICA, "BJ"),
    BKN("BKN", "Burkina", Region.WEST_TROPICAL_AFRICA, "BF"),
    GAM("GAM", "Gambia, The", Region.WEST_TROPICAL_AFRICA, "GM"),
    GHA("GHA", "Ghana", Region.WEST_TROPICAL_AFRICA, "GH"),
    GNB("GNB", "Guinea-Bissau", Region.WEST_TROPICAL_AFRICA, "GW"),
    GUI("GUI", "Guinea", Region.WEST_TROPICAL_AFRICA, "GN"),
    IVO("IVO", "Ivory Coast", Region.WEST_TROPICAL_AFRICA, "CI"),
    LBR("LBR", "Liberia", Region.WEST_TROPICAL_AFRICA, "LR"),
    MLI("MLI", "Mali", Region.WEST_TROPICAL_AFRICA, "ML"),
    MTN("MTN", "Mauritania", Region.WEST_TROPICAL_AFRICA, "MR"),
    NGA("NGA", "Nigeria", Region.WEST_TROPICAL_AFRICA, "NG"),
    NGR("NGR", "Niger", Region.WEST_TROPICAL_AFRICA, "NE"),
    SEN("SEN", "Senegal", Region.WEST_TROPICAL_AFRICA, "SN"),
    SIE("SIE", "Sierra Leone", Region.WEST_TROPICAL_AFRICA, "SL"),
    TOG("TOG", "Togo", Region.WEST_TROPICAL_AFRICA, "TG"),
    BUR("BUR", "Burundi", Region.WESTCENTRAL_TROPICAL_AFRICA, "BI"),
    CAB("CAB", "Cabinda", Region.WESTCENTRAL_TROPICAL_AFRICA, "AO"),
    CAF("CAF", "Central African Republic", Region.WESTCENTRAL_TROPICAL_AFRICA, "CF"),
    CMN("CMN", "Cameroon", Region.WESTCENTRAL_TROPICAL_AFRICA, "CM"),
    CON("CON", "Congo", Region.WESTCENTRAL_TROPICAL_AFRICA, "CG"),
    EQG("EQG", "Equatorial Guinea", Region.WESTCENTRAL_TROPICAL_AFRICA, "GQ"),
    GAB("GAB", "Gabon", Region.WESTCENTRAL_TROPICAL_AFRICA, "GA"),
    GGI("GGI", "Gulf of Guinea Is.", Region.WESTCENTRAL_TROPICAL_AFRICA),
    RWA("RWA", "Rwanda", Region.WESTCENTRAL_TROPICAL_AFRICA, "RW"),
    ZAI("ZAI", "Zaire", Region.WESTCENTRAL_TROPICAL_AFRICA, "CD"),
    CHA("CHA", "Chad", Region.NORTHEAST_TROPICAL_AFRICA, "TD"),
    DJI("DJI", "Djibouti", Region.NORTHEAST_TROPICAL_AFRICA, "DJ"),
    ERI("ERI", "Eritrea", Region.NORTHEAST_TROPICAL_AFRICA, "ER"),
    ETH("ETH", "Ethiopia", Region.NORTHEAST_TROPICAL_AFRICA, "ET"),
    SOC("SOC", "Socotra", Region.NORTHEAST_TROPICAL_AFRICA, "YE"),
    SOM("SOM", "Somalia", Region.NORTHEAST_TROPICAL_AFRICA, "SO"),
    SUD("SUD", "Sudan", Region.NORTHEAST_TROPICAL_AFRICA, "SD"),
    KEN("KEN", "Kenya", Region.EAST_TROPICAL_AFRICA, "KE"),
    TAN("TAN", "Tanzania", Region.EAST_TROPICAL_AFRICA, "TZ"),
    UGA("UGA", "Uganda", Region.EAST_TROPICAL_AFRICA, "UG"),
    ANG("ANG", "Angola", Region.SOUTH_TROPICAL_AFRICA, "AO"),
    MLW("MLW", "Malawi", Region.SOUTH_TROPICAL_AFRICA, "MW"),
    MOZ("MOZ", "Mozambique", Region.SOUTH_TROPICAL_AFRICA, "MZ"),
    ZAM("ZAM", "Zambia", Region.SOUTH_TROPICAL_AFRICA, "ZM"),
    ZIM("ZIM", "Zimbabwe", Region.SOUTH_TROPICAL_AFRICA, "ZW"),
    BOT("BOT", "Botswana", Region.SOUTHERN_AFRICA, "BW"),
    CPP("CPP", "Cape Provinces", Region.SOUTHERN_AFRICA, "ZA"),
    CPV("CPV", "Caprivi Strip", Region.SOUTHERN_AFRICA, "NA"),
    LES("LES", "Lesotho", Region.SOUTHERN_AFRICA, "LS"),
    NAM("NAM", "Namibia", Region.SOUTHERN_AFRICA, "ZA"),
    NAT("NAT", "KwaZulu-Natal", Region.SOUTHERN_AFRICA, "ZA"),
    OFS("OFS", "Free State", Region.SOUTHERN_AFRICA, "ZA"),
    SWZ("SWZ", "Swaziland", Region.SOUTHERN_AFRICA, "SZ"),
    TVL("TVL", "Northern Provinces", Region.SOUTHERN_AFRICA, "ZA"),
    ASC("ASC", "Ascension", Region.MIDDLE_ATLANTIC_OCEAN, "SH"),
    STH("STH", "St.Helena", Region.MIDDLE_ATLANTIC_OCEAN, "SH"),
    ALD("ALD", "Aldabra", Region.WESTERN_INDIAN_OCEAN, "SC"),
    CGS("CGS", "Chagos Archipelago", Region.WESTERN_INDIAN_OCEAN, "IO"),
    COM("COM", "Comoros", Region.WESTERN_INDIAN_OCEAN),
    MAU("MAU", "Mauritius", Region.WESTERN_INDIAN_OCEAN, "MU"),
    MCI("MCI", "Mozambique Channel Is.", Region.WESTERN_INDIAN_OCEAN, "RE"),
    MDG("MDG", "Madagascar", Region.WESTERN_INDIAN_OCEAN, "MG"),
    REU("REU", "Réunion", Region.WESTERN_INDIAN_OCEAN, "RE"),
    ROD("ROD", "Rodrigues", Region.WESTERN_INDIAN_OCEAN, "MU"),
    SEY("SEY", "Seychelles", Region.WESTERN_INDIAN_OCEAN, "SC"),
    ALT("ALT", "Altay", Region.SIBERIA, "RU"),
    BRY("BRY", "Buryatiya", Region.SIBERIA, "RU"),
    CTA("CTA", "Chita", Region.SIBERIA, "RU"),
    IRK("IRK", "Irkutsk", Region.SIBERIA, "RU"),
    KRA("KRA", "Krasnoyarsk", Region.SIBERIA, "RU"),
    TVA("TVA", "Tuva", Region.SIBERIA, "RU"),
    WSB("WSB", "West Siberia", Region.SIBERIA, "RU"),
    YAK("YAK", "Yakutskiya", Region.SIBERIA, "RU"),
    AMU("AMU", "Amur", Region.RUSSIA_FAR_EAST, "RU"),
    KAM("KAM", "Kamchatka", Region.RUSSIA_FAR_EAST, "RU"),
    KHA("KHA", "Khabarovsk", Region.RUSSIA_FAR_EAST, "RU"),
    KUR("KUR", "Kuril Is.", Region.RUSSIA_FAR_EAST, "RU"),
    MAG("MAG", "Magadan", Region.RUSSIA_FAR_EAST, "RU"),
    PRM("PRM", "Primorye", Region.RUSSIA_FAR_EAST, "RU"),
    SAK("SAK", "Sakhalin", Region.RUSSIA_FAR_EAST, "RU"),
    KAZ("KAZ", "Kazakhstan", Region.MIDDLE_ASIA, "KZ"),
    KGZ("KGZ", "Kirgizistan", Region.MIDDLE_ASIA, "KG"),
    TKM("TKM", "Turkmenistan", Region.MIDDLE_ASIA, "TM"),
    TZK("TZK", "Tadzhikistan", Region.MIDDLE_ASIA, "TJ"),
    UZB("UZB", "Uzbekistan", Region.MIDDLE_ASIA, "UZ"),
    NCS("NCS", "North Caucasus", Region.CAUCASUS, "RU"),
    TCS("TCS", "Transcaucasus", Region.CAUCASUS),
    AFG("AFG", "Afghanistan", Region.WESTERN_ASIA, "AF"),
    CYP("CYP", "Cyprus", Region.WESTERN_ASIA, "CY"),
    EAI("EAI", "East Aegean Is.", Region.WESTERN_ASIA, "GR"),
    IRN("IRN", "Iran", Region.WESTERN_ASIA, "IR"),
    IRQ("IRQ", "Iraq", Region.WESTERN_ASIA, "IQ"),
    LBS("LBS", "Lebanon-Syria", Region.WESTERN_ASIA),
    PAL("PAL", "Palestine", Region.WESTERN_ASIA),
    SIN("SIN", "Sinai", Region.WESTERN_ASIA, "EG"),
    TUR("TUR", "Turkey", Region.WESTERN_ASIA, "TR"),
    GST("GST", "Gulf States", Region.ARABIAN_PENINSULA),
    KUW("KUW", "Kuwait", Region.ARABIAN_PENINSULA, "KW"),
    OMA("OMA", "Oman", Region.ARABIAN_PENINSULA, "OM"),
    SAU("SAU", "Saudi Arabia", Region.ARABIAN_PENINSULA, "SA"),
    YEM("YEM", "Yemen", Region.ARABIAN_PENINSULA, "YE"),
    CHC("CHC", "China South-Central", Region.CHINA, "CN"),
    CHH("CHH", "Hainan", Region.CHINA, "CN"),
    CHI("CHI", "Inner Mongolia", Region.CHINA, "CN"),
    CHM("CHM", "Manchuria", Region.CHINA, "CN"),
    CHN("CHN", "China North-Central", Region.CHINA, "CN"),
    CHQ("CHQ", "Qinghai", Region.CHINA, "CN"),
    CHS("CHS", "China Southeast", Region.CHINA),
    CHT("CHT", "Tibet", Region.CHINA, "CN"),
    CHX("CHX", "Xinjiang", Region.CHINA, "CN"),
    MON("MON", "Mongolia", Region.MONGOLIA, "MN"),
    JAP("JAP", "Japan", Region.EASTERN_ASIA, "JP"),
    KOR("KOR", "Korea", Region.EASTERN_ASIA),
    KZN("KZN", "Kazan-retto", Region.EASTERN_ASIA, "JP"),
    NNS("NNS", "Nansei-shoto", Region.EASTERN_ASIA, "JP"),
    OGA("OGA", "Ogasawara-shoto", Region.EASTERN_ASIA, "JP"),
    TAI("TAI", "Taiwan", Region.EASTERN_ASIA, "TW"),
    ASS("ASS", "Assam", Region.INDIAN_SUBCONTINENT, "IN"),
    BAN("BAN", "Bangladesh", Region.INDIAN_SUBCONTINENT, "BD"),
    EHM("EHM", "East Himalaya", Region.INDIAN_SUBCONTINENT),
    IND("IND", "India", Region.INDIAN_SUBCONTINENT, "IN"),
    LDV("LDV", "Laccadive Is.", Region.INDIAN_SUBCONTINENT, "IN"),
    MDV("MDV", "Maldives", Region.INDIAN_SUBCONTINENT, "MV"),
    NEP("NEP", "Nepal", Region.INDIAN_SUBCONTINENT, "NP"),
    PAK("PAK", "Pakistan", Region.INDIAN_SUBCONTINENT, "PK"),
    SRL("SRL", "Sri Lanka", Region.INDIAN_SUBCONTINENT, "LK"),
    WHM("WHM", "West Himalaya", Region.INDIAN_SUBCONTINENT),
    AND("AND", "Andaman Is.", Region.INDOCHINA),
    CBD("CBD", "Cambodia", Region.INDOCHINA, "KH"),
    LAO("LAO", "Laos", Region.INDOCHINA, "LA"),
    MYA("MYA", "Myanmar", Region.INDOCHINA, "MM"),
    NCB("NCB", "Nicobar Is.", Region.INDOCHINA, "IN"),
    SCS("SCS", "South China Sea", Region.INDOCHINA),
    THA("THA", "Thailand", Region.INDOCHINA, "TH"),
    VIE("VIE", "Vietnam", Region.INDOCHINA, "VN"),
    BOR("BOR", "Borneo", Region.MALESIA),
    CKI("CKI", "Cocos (Keeling) Is.", Region.MALESIA, "CC"),
    JAW("JAW", "Jawa", Region.MALESIA, "ID"),
    LSI("LSI", "Lesser Sunda Is.", Region.MALESIA),
    MLY("MLY", "Malaya", Region.MALESIA),
    MOL("MOL", "Maluku", Region.MALESIA, "ID"),
    PHI("PHI", "Philippines", Region.MALESIA, "PH"),
    SUL("SUL", "Sulawesi", Region.MALESIA, "ID"),
    SUM("SUM", "Sumatera", Region.MALESIA, "ID"),
    XMS("XMS", "Christmas I.", Region.MALESIA, "CX"),
    BIS("BIS", "Bismarck Archipelago", Region.PAPUASIA, "PG"),
    NWG("NWG", "New Guinea", Region.PAPUASIA),
    SOL("SOL", "Solomon Is.", Region.PAPUASIA),
    NFK("NFK", "Norfolk Is.", Region.AUSTRALASIA),
    NSW("NSW", "New South Wales", Region.AUSTRALASIA, "AU"),
    NTA("NTA", "Northern Territory", Region.AUSTRALASIA, "AU"),
    QLD("QLD", "Queensland", Region.AUSTRALASIA, "AU"),
    SOA("SOA", "South Australia", Region.AUSTRALASIA, "AU"),
    TAS("TAS", "Tasmania", Region.AUSTRALASIA, "AU"),
    VIC("VIC", "Victoria", Region.AUSTRALASIA, "AU"),
    WAU("WAU", "Western Australia", Region.AUSTRALASIA, "AU"),
    ATP("ATP", "Antipodean Is.", Region.NEW_ZEALAND, "NZ"),
    CTM("CTM", "Chatham Is.", Region.NEW_ZEALAND, "NZ"),
    KER("KER", "Kermadec Is.", Region.NEW_ZEALAND, "NZ"),
    NZN("NZN", "New Zealand North", Region.NEW_ZEALAND, "NZ"),
    NZS("NZS", "New Zealand South", Region.NEW_ZEALAND, "NZ"),
    FIJ("FIJ", "Fiji", Region.SOUTHWESTERN_PACIFIC, "FJ"),
    GIL("GIL", "Gilbert Is.", Region.SOUTHWESTERN_PACIFIC, "KI"),
    HBI("HBI", "Howland-Baker Is.", Region.SOUTHWESTERN_PACIFIC, "UM"),
    NRU("NRU", "Nauru", Region.SOUTHWESTERN_PACIFIC, "NR"),
    NUE("NUE", "Niue", Region.SOUTHWESTERN_PACIFIC, "NU"),
    NWC("NWC", "New Caledonia", Region.SOUTHWESTERN_PACIFIC, "NC"),
    PHX("PHX", "Phoenix Is.", Region.SOUTHWESTERN_PACIFIC, "KI"),
    SAM("SAM", "Samoa", Region.SOUTHWESTERN_PACIFIC),
    SCZ("SCZ", "Santa Cruz Is.", Region.SOUTHWESTERN_PACIFIC, "SB"),
    TOK("TOK", "Tokelau-Manihiki", Region.SOUTHWESTERN_PACIFIC),
    TON("TON", "Tonga", Region.SOUTHWESTERN_PACIFIC, "TO"),
    TUV("TUV", "Tuvalu", Region.SOUTHWESTERN_PACIFIC, "TV"),
    VAN("VAN", "Vanuatu", Region.SOUTHWESTERN_PACIFIC, "VU"),
    WAL("WAL", "Wallis-Futuna Is.", Region.SOUTHWESTERN_PACIFIC, "WF"),
    COO("COO", "Cook Is.", Region.SOUTHCENTRAL_PACIFIC, "CK"),
    EAS("EAS", "Easter Is.", Region.SOUTHCENTRAL_PACIFIC, "CL"),
    LIN("LIN", "Line Is.", Region.SOUTHCENTRAL_PACIFIC),
    MRQ("MRQ", "Marquesas", Region.SOUTHCENTRAL_PACIFIC, "PF"),
    PIT("PIT", "Pitcairn Is.", Region.SOUTHCENTRAL_PACIFIC, "PN"),
    SCI("SCI", "Society Is.", Region.SOUTHCENTRAL_PACIFIC, "PF"),
    TUA("TUA", "Tuamotu", Region.SOUTHCENTRAL_PACIFIC, "PF"),
    TUB("TUB", "Tubuai Is.", Region.SOUTHCENTRAL_PACIFIC, "PF"),
    CRL("CRL", "Caroline Is.", Region.NORTHWESTERN_PACIFIC),
    MCS("MCS", "Marcus I.", Region.NORTHWESTERN_PACIFIC, "JP"),
    MRN("MRN", "Marianas", Region.NORTHWESTERN_PACIFIC),
    MRS("MRS", "Marshall Is.", Region.NORTHWESTERN_PACIFIC, "MH"),
    WAK("WAK", "Wake I.", Region.NORTHWESTERN_PACIFIC, "UM"),
    HAW("HAW", "Hawaii", Region.NORTHCENTRAL_PACIFIC),
    ALU("ALU", "Aleutian Is.", Region.SUBARCTIC_AMERICA, "US"),
    ASK("ASK", "Alaska", Region.SUBARCTIC_AMERICA, "US"),
    GNL("GNL", "Greenland", Region.SUBARCTIC_AMERICA, "GL"),
    NUN("NUN", "Nunavut", Region.SUBARCTIC_AMERICA, "CA"),
    NWT("NWT", "Northwest Territories", Region.SUBARCTIC_AMERICA, "CA"),
    YUK("YUK", "Yukon", Region.SUBARCTIC_AMERICA, "CA"),
    ABT("ABT", "Alberta", Region.WESTERN_CANADA, "CA"),
    BRC("BRC", "British Columbia", Region.WESTERN_CANADA, "CA"),
    MAN("MAN", "Manitoba", Region.WESTERN_CANADA, "CA"),
    SAS("SAS", "Saskatchewan", Region.WESTERN_CANADA, "CA"),
    LAB("LAB", "Labrador", Region.EASTERN_CANADA, "CA"),
    NBR("NBR", "New Brunswick", Region.EASTERN_CANADA, "CA"),
    NFL("NFL", "Newfoundland", Region.EASTERN_CANADA),
    NSC("NSC", "Nova Scotia", Region.EASTERN_CANADA, "CA"),
    ONT("ONT", "Ontario", Region.EASTERN_CANADA, "CA"),
    PEI("PEI", "Prince Edward I.", Region.EASTERN_CANADA, "CA"),
    QUE("QUE", "Québec", Region.EASTERN_CANADA, "CA"),
    COL("COL", "Colorado", Region.NORTHWESTERN_USA, "US"),
    IDA("IDA", "Idaho", Region.NORTHWESTERN_USA, "US"),
    MNT("MNT", "Montana", Region.NORTHWESTERN_USA, "US"),
    ORE("ORE", "Oregon", Region.NORTHWESTERN_USA, "US"),
    WAS("WAS", "Washington", Region.NORTHWESTERN_USA, "US"),
    WYO("WYO", "Wyoming", Region.NORTHWESTERN_USA, "US"),
    ILL("ILL", "Illinois", Region.NORTHCENTRAL_USA, "US"),
    IOW("IOW", "Iowa", Region.NORTHCENTRAL_USA, "US"),
    KAN("KAN", "Kansas", Region.NORTHCENTRAL_USA, "US"),
    MIN("MIN", "Minnesota", Region.NORTHCENTRAL_USA, "US"),
    MSO("MSO", "Missouri", Region.NORTHCENTRAL_USA, "US"),
    NDA("NDA", "North Dakota", Region.NORTHCENTRAL_USA, "US"),
    NEB("NEB", "Nebraska", Region.NORTHCENTRAL_USA, "US"),
    OKL("OKL", "Oklahoma", Region.NORTHCENTRAL_USA, "US"),
    SDA("SDA", "South Dakota", Region.NORTHCENTRAL_USA, "US"),
    WIS("WIS", "Wisconsin", Region.NORTHCENTRAL_USA, "US"),
    CNT("CNT", "Connecticut", Region.NORTHEASTERN_USA, "US"),
    INI("INI", "Indiana", Region.NORTHEASTERN_USA, "US"),
    MAI("MAI", "Maine", Region.NORTHEASTERN_USA, "US"),
    MAS("MAS", "Masachusettes", Region.NORTHEASTERN_USA, "US"),
    MIC("MIC", "Michigan", Region.NORTHEASTERN_USA, "US"),
    NWH("NWH", "New Hampshire", Region.NORTHEASTERN_USA, "US"),
    NWJ("NWJ", "New Jersey", Region.NORTHEASTERN_USA, "US"),
    NWY("NWY", "New York", Region.NORTHEASTERN_USA, "US"),
    OHI("OHI", "Ohio", Region.NORTHEASTERN_USA, "US"),
    PEN("PEN", "Pennsylvania", Region.NORTHEASTERN_USA, "US"),
    RHO("RHO", "Rhode I.", Region.NORTHEASTERN_USA, "US"),
    VER("VER", "Vermont", Region.NORTHEASTERN_USA, "US"),
    WVA("WVA", "West Virginia", Region.NORTHEASTERN_USA, "US"),
    ARI("ARI", "Arizona", Region.SOUTHWESTERN_USA, "US"),
    CAL("CAL", "California", Region.SOUTHWESTERN_USA, "US"),
    NEV("NEV", "Nevada", Region.SOUTHWESTERN_USA, "US"),
    UTA("UTA", "Utah", Region.SOUTHWESTERN_USA, "US"),
    NWM("NWM", "New Mexico", Region.SOUTHCENTRAL_USA, "US"),
    TEX("TEX", "Texas", Region.SOUTHCENTRAL_USA, "US"),
    ALA("ALA", "Alabama", Region.SOUTHEASTERN_USA, "US"),
    ARK("ARK", "Arkansas", Region.SOUTHEASTERN_USA, "US"),
    DEL("DEL", "Delaware", Region.SOUTHEASTERN_USA, "US"),
    FLA("FLA", "Florida", Region.SOUTHEASTERN_USA, "US"),
    GEO("GEO", "Georgia", Region.SOUTHEASTERN_USA, "US"),
    KTY("KTY", "Kentucky", Region.SOUTHEASTERN_USA, "US"),
    LOU("LOU", "Louisiana", Region.SOUTHEASTERN_USA, "US"),
    MRY("MRY", "Maryland", Region.SOUTHEASTERN_USA, "US"),
    MSI("MSI", "Mississippi", Region.SOUTHEASTERN_USA, "US"),
    NCA("NCA", "North Carolina", Region.SOUTHEASTERN_USA, "US"),
    SCA("SCA", "South Carolina", Region.SOUTHEASTERN_USA, "US"),
    TEN("TEN", "Tennessee", Region.SOUTHEASTERN_USA, "US"),
    VRG("VRG", "Virginia", Region.SOUTHEASTERN_USA, "US"),
    WDC("WDC", "District of Columbia", Region.SOUTHEASTERN_USA, "US"),
    MXC("MXC", "Mexico Central", Region.MEXICO, "MX"),
    MXE("MXE", "Mexico Northeast", Region.MEXICO, "MX"),
    MXG("MXG", "Mexico Gulf", Region.MEXICO, "MX"),
    MXI("MXI", "Mexican Pacific Is.", Region.MEXICO, "MX"),
    MXN("MXN", "Mexico Northwest", Region.MEXICO, "MX"),
    MXS("MXS", "Mexico Southwest", Region.MEXICO, "MX"),
    MXT("MXT", "Mexico Southeast", Region.MEXICO, "MX"),
    BLZ("BLZ", "Belize", Region.CENTRAL_AMERICA, "BZ"),
    COS("COS", "Costa Rica", Region.CENTRAL_AMERICA, "CR"),
    CPI("CPI", "Central American Pacific Is.", Region.CENTRAL_AMERICA),
    ELS("ELS", "El Salvador", Region.CENTRAL_AMERICA, "SV"),
    GUA("GUA", "Guatemala", Region.CENTRAL_AMERICA, "GT"),
    HON("HON", "Honduras", Region.CENTRAL_AMERICA, "HN"),
    NIC("NIC", "Nicaragua", Region.CENTRAL_AMERICA, "NI"),
    PAN("PAN", "Panamá", Region.CENTRAL_AMERICA, "PA"),
    ARU("ARU", "Aruba", Region.CARIBBEAN, "AW"),
    BAH("BAH", "Bahamas", Region.CARIBBEAN, "BS"),
    BER("BER", "Bermuda", Region.CARIBBEAN, "BM"),
    CAY("CAY", "Cayman Is.", Region.CARIBBEAN, "KY"),
    CUB("CUB", "Cuba", Region.CARIBBEAN, "CU"),
    DOM("DOM", "Dominican Republic", Region.CARIBBEAN, "DO"),
    HAI("HAI", "Haiti", Region.CARIBBEAN),
    JAM("JAM", "Jamaica", Region.CARIBBEAN, "JM"),
    LEE("LEE", "Leeward Is.", Region.CARIBBEAN),
    NLA("NLA", "Netherlands Antilles", Region.CARIBBEAN, "AN"),
    PUE("PUE", "Puerto Rico", Region.CARIBBEAN, "PR"),
    SWC("SWC", "Southwest Caribbean", Region.CARIBBEAN),
    TCI("TCI", "Turks-Caicos Is.", Region.CARIBBEAN, "TC"),
    TRT("TRT", "Trinidad-Tobago", Region.CARIBBEAN, "TT"),
    VNA("VNA", "Venezuelan Antilles", Region.CARIBBEAN, "VE"),
    WIN("WIN", "Windward Is.", Region.CARIBBEAN),
    FRG("FRG", "French Guiana", Region.NORTHERN_SOUTH_AMERICA, "GF"),
    GUY("GUY", "Guyana", Region.NORTHERN_SOUTH_AMERICA, "GY"),
    SUR("SUR", "Suriname", Region.NORTHERN_SOUTH_AMERICA, "SR"),
    VEN("VEN", "Venezuela", Region.NORTHERN_SOUTH_AMERICA, "VE"),
    BOL("BOL", "Bolivia", Region.WESTERN_SOUTH_AMERICA, "BO"),
    CLM("CLM", "Colombia", Region.WESTERN_SOUTH_AMERICA, "CO"),
    ECU("ECU", "Ecuador", Region.WESTERN_SOUTH_AMERICA, "EC"),
    GAL("GAL", "Galápagos", Region.WESTERN_SOUTH_AMERICA, "EC"),
    PER("PER", "Peru", Region.WESTERN_SOUTH_AMERICA, "PE"),
    BZC("BZC", "Brazil West-Central", Region.BRAZIL, "BR"),
    BZE("BZE", "Brazil Northeast", Region.BRAZIL, "BR"),
    BZL("BZL", "Brazil Southeast", Region.BRAZIL, "BR"),
    BZN("BZN", "Brazil North", Region.BRAZIL, "BR"),
    BZS("BZS", "Brazil South", Region.BRAZIL, "BR"),
    AGE("AGE", "Argentina Northeast", Region.SOUTHERN_SOUTH_AMERICA, "AR"),
    AGS("AGS", "Argentina South", Region.SOUTHERN_SOUTH_AMERICA, "AR"),
    AGW("AGW", "Argentina Northwest", Region.SOUTHERN_SOUTH_AMERICA, "AR"),
    CLC("CLC", "Chile Central", Region.SOUTHERN_SOUTH_AMERICA, "CL"),
    CLN("CLN", "Chile North", Region.SOUTHERN_SOUTH_AMERICA, "CL"),
    CLS("CLS", "Chile South", Region.SOUTHERN_SOUTH_AMERICA, "CL"),
    DSV("DSV", "Desventurados Is.", Region.SOUTHERN_SOUTH_AMERICA, "CL"),
    JNF("JNF", "Juan Fernández Is.", Region.SOUTHERN_SOUTH_AMERICA, "CL"),
    PAR("PAR", "Paraguay", Region.SOUTHERN_SOUTH_AMERICA, "PY"),
    URU("URU", "Uruguay", Region.SOUTHERN_SOUTH_AMERICA, "UY"),
    ASP("ASP", "Amsterdam-St.Paul Is.", Region.SUBANTARCTIC_ISLANDS, "TF"),
    BOU("BOU", "Bouvet I.", Region.SUBANTARCTIC_ISLANDS, "BV"),
    CRZ("CRZ", "Crozet Is.", Region.SUBANTARCTIC_ISLANDS, "TF"),
    FAL("FAL", "Falkland Is.", Region.SUBANTARCTIC_ISLANDS, "FK"),
    HMD("HMD", "Heard-McDonald Is.", Region.SUBANTARCTIC_ISLANDS, "HM"),
    KEG("KEG", "Kerguelen", Region.SUBANTARCTIC_ISLANDS, "TF"),
    MAQ("MAQ", "Macquarie Is.", Region.SUBANTARCTIC_ISLANDS, "AU"),
    MPE("MPE", "Marion-Prince Edward Is.", Region.SUBANTARCTIC_ISLANDS, "ZA"),
    SGE("SGE", "South Georgia", Region.SUBANTARCTIC_ISLANDS, "GS"),
    SSA("SSA", "South Sandwich Is.", Region.SUBANTARCTIC_ISLANDS, "GS"),
    TDC("TDC", "Tristan da Cunha", Region.SUBANTARCTIC_ISLANDS, "SH"),
    ANT("ANT", "Antarctica", Region.ANTARCTIC_CONTINENT, "AQ");

    /**
     * The length of the country code string.
     */
    public static final int COUNTRY_CODE_LENGTH = 2;
    /**
     *
     */
    private String name;

    /**
     *
     */
    private Region region;

    /**
     *
     */
    private String isoCode;

    /**
     *
     */
    private String code;

    /**
     *
     * @param newCode Set the code of this country
     * @param newName Set the name of this country
     * @param newRegion Set the region this counry is in
     * @param newIsoCode Set the ISO code of this country
     */
    private Country(final String newCode, final String newName,
            final Region newRegion, final String newIsoCode) {
        this(newCode, newName, newRegion);
        this.isoCode = newIsoCode;
    }

    /**
    *
    * @param newCode Set the code of this country
    * @param newName Set the name of this country
    * @param newRegion Set the region this counry is in
    */
    private Country(final String newCode, final String newName,
            final Region newRegion) {
        this.code = newCode;
        this.name = newName;
        this.region = newRegion;
    }

    /**
     *
     * @return The name of this country
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The Region this counry is in
     */
    public Region getRegion() {
        return region;
    }

    /**
     *
     * @return The ISO code of this country or null if this country does not
     *         have an ISO code
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     *
     * @return The TDWG code of this country
     */
    public String getCode() {
        return code;
    }

   /**
    *
    * @param code the code of the country
    * @return a valid country
    */
   public static Country fromString(final String code) {
       for (Country country : Country.values()) {
           if (country.code.equals(code)) {
               return country;
           }
       }
       throw new IllegalArgumentException(code
               + " is not a valid Country code");
   }

   @Override
   public String toString() {
       return code;
   }

  /**
   *
   * @param other the other region
   * @return 1 if other is after this, -1 if other is before this and 0 if
   *         other is equal to this
   */
   public int compareNames(final Country other) {
       return this.name.compareTo(other.name);
   }
}
