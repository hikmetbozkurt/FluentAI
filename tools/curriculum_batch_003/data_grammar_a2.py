#!/usr/bin/env python3
"""
Grammar Batch 003: A2 Lessons (5 lessons).
"""

from typing import List, Dict, Any

A2_LESSONS: List[Dict[str, Any]] = [
    {
        "id": "grammar.a2.have-got-vs-have",
        "title": "Possession and States: 'Have got' vs. 'Have'",
        "cefr_level": "A2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Both 'have got' and 'have' express possession, family relationships, and personal characteristics in the present tense, but they form questions and negatives differently.",
        "summary_tr": "'Have got' ve 'have' yapıları geniş zamanda sahiplik, aile ilişkileri ve kişisel özellikleri belirtir; ancak soru ve olumsuz yapılış kuralları tamamen farklıdır.",
        "explanation_en": [
            {
                "title": "Forming Statements, Negatives, and Questions",
                "content": "'Have got' is particularly common in British English and informal conversation. In negative sentences, 'have got' takes 'haven't / hasn't got', whereas 'have' uses the auxiliary 'don't / doesn't have'. In questions, 'have got' inverts ('Have you got a pen?'), while 'have' requires do/does ('Do you have a pen?').",
                "patterns": [
                    "Affirmative: Subject + have/has got + Noun OR Subject + have/has + Noun",
                    "Negative: Subject + haven't/hasn't got + Noun OR Subject + don't/doesn't have + Noun",
                    "Interrogative: Have/Has + Subject + got + Noun? OR Do/Does + Subject + have + Noun?"
                ]
            }
        ],
        "explanation_tr": "Türkçede her iki kullanım da 'sahip olmak' veya '-im var' ekiyle karşılanır. Türk öğrencilerin en sık yaptığı hata, iki yapıyı birbirine karıştırarak 'Do you have got...?' veya 'I don't have got...' şeklinde melez yapılar kurmalarıdır. 'Do/does' kullanılıyorsa 'got' asla eklenmez.",
        "rules": [
            {
                "name": "Auxiliary Separation Rule",
                "pattern": "Do/Does + Subject + have (NO got) vs. Have/Has + Subject + got",
                "use_cases": [
                    "Describing office supplies, equipment, and personal belongings",
                    "Inquiring about someone's schedule, family, or health symptoms"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Hybrid Auxiliary Trap (Do you have got)",
                "description_tr": "Türkçe düşünürken 'got' kelimesini zorunlu bir parça sanıp soru yardımcı fiili 'do' ile birleştirmek ciddi bir dilbilgisi hatasıdır.",
                "trap_example": "Do you have got any spare monitor cables in the storage?",
                "correction": "Do you have any spare monitor cables...? OR Have you got any spare monitor cables...?",
                "key_difference_tr": "'Do' yardımcı fiili devreye girdiğinde 'got' kelimesi düşer; 'got' varsa başa 'have' gelir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "have got (British / informal)",
                "concept_b": "have (Standard / universal / American)",
                "difference_en": "'Have got' is present tense only and sounds more colloquial; 'have' works across all tenses.",
                "difference_tr": "'Have got' sadece şimdiki/geniş zamanda kullanılır ve geçmiş zamanı (had got) sahiplik için tercih edilmez; 'have' ise geçmişte 'had' olur.",
                "example_a": "I've got a bad headache today.",
                "example_b": "I have two meetings scheduled this afternoon."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "She hasn't a laptop today.",
                "correct": "She doesn't have a laptop today. / She hasn't got a laptop today.",
                "explanation_en": "In modern English, 'has not' alone without 'got' is archaic when denoting possession.",
                "explanation_tr": "Sahiplik anlamında 'has not' tek başına kullanılmaz; ya 'doesn't have' ya da 'hasn't got' denmelidir."
            }
        ],
        "examples": [
            {
                "en": "Have you got the conference room keys with you?",
                "tr": "Konferans odasının anahtarları yanında mı?",
                "context": "Asking a colleague in the office corridor",
                "register": "informal_spoken",
                "highlighted_phrase": "Have you got"
            },
            {
                "en": "Our department doesn't have enough budget for external travel this quarter.",
                "tr": "Departmanımızın bu çeyrekte dış seyahatler için yeterli bütçesi yok.",
                "context": "Budget meeting discussion",
                "register": "neutral_workplace",
                "highlighted_phrase": "doesn't have"
            },
            {
                "en": "He has got three years of experience in mobile application development.",
                "tr": "Mobil uygulama geliştirmede üç yıllık deneyimi var.",
                "context": "Reviewing candidate profile",
                "register": "neutral_workplace",
                "highlighted_phrase": "has got"
            },
            {
                "en": "Do you have time for a quick phone call before lunch?",
                "tr": "Öğle yemeğinden önce kısa bir telefon görüşmesi için vaktin var mı?",
                "context": "Scheduling a discussion with a manager",
                "register": "formal_written",
                "highlighted_phrase": "Do you have"
            }
        ],
        "topic_tags": ["work-career", "communication"]
    },
    {
        "id": "grammar.a2.imperatives-and-polite-requests",
        "title": "Imperatives and Polite Requests: Directives vs. 'Could you / Would you'",
        "cefr_level": "A2",
        "category": "modals_and_semi_modals",
        "summary_en": "Imperatives give direct orders or instructions using base verb forms, whereas polite requests soften directives using modal verbs like 'Could you' or 'Would you'.",
        "summary_tr": "Emir kipleri (imperatives) yalın fiille doğrudan talimat veya yönlendirme verirken; 'Could you' ve 'Would you' gibi modallar iş ortamında kibar ricalar oluşturur.",
        "explanation_en": [
            {
                "title": "Direct Imperative vs. Polite Request Modals",
                "content": "Imperatives start directly with the bare infinitive ('Press the green button', 'Do not enter'). While useful for technical manuals, signs, and step-by-step instructions, using bare imperatives toward colleagues can sound blunt or rude. Adding 'please', or converting to 'Could you please + verb' or 'Would you mind + -ing', provides appropriate social politeness.",
                "patterns": [
                    "Direct instruction: Base Verb + Object (e.g., Save your document frequently)",
                    "Negative instruction: Do not / Don't + Base Verb (e.g., Don't unplug the cable)",
                    "Polite request: Could you (please) + Base Verb? (e.g., Could you send me the invoice?)"
                ]
            }
        ],
        "explanation_tr": "Türkçede rica ve emir arasındaki fark fiil sonundaki saygı ekleriyle (-ebilir misiniz, bakar mısınız) sağlanır. İngilizcede sadece fiili yalın söylemek ('Send me the file') çok kaba algılanabilir. Profesyonel iletişimde 'Could you please send...' kalıbı standarttır.",
        "rules": [
            {
                "name": "Imperative Base Form",
                "pattern": "Base Verb + Object / Don't + Base Verb",
                "use_cases": [
                    "Software setup guides and technical instructions",
                    "Safety notices and standard operating procedures"
                ]
            },
            {
                "name": "Polite Request Modal Pattern",
                "pattern": "Could you / Would you + please + base verb + ...?",
                "use_cases": [
                    "Requesting files, approvals, or assistance from colleagues and clients",
                    "Asking someone to repeat or clarify information"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Blunt Command Transfer Trap",
                "description_tr": "Türkçedeki 'Bana dosyayı gönderin' nezaketini yalın İngilizce emir kipi sanıp 'Send me the file' demek kaba ve emrivaki duyulur.",
                "trap_example": "Send me the updated spreadsheet immediately.",
                "correction": "Could you please send me the updated spreadsheet when you have a moment?",
                "key_difference_tr": "İngilizcede iş yerinde 'Could you please...' veya 'Please send...' kalıpları tercih edilir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Imperative (Direct)",
                "concept_b": "Polite Request (Indirect)",
                "difference_en": "Imperatives instruct without negotiation; polite requests give the listener social deference.",
                "difference_tr": "Emir kipi doğrudan işlem bildirir; kibar rica ise karşı tarafa saygılı bir alan tanır.",
                "example_a": "Review paragraph three carefully.",
                "example_b": "Could you please review paragraph three when you get a chance?"
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "You send the report now, please.",
                "correct": "Please send the report now. / Could you send the report now?",
                "explanation_en": "Do not include the pronoun 'you' in standard imperative directives.",
                "explanation_tr": "Emir kipinde cümlenin başına 'You' zamiri konulmaz, doğrudan fiille başlanır."
            }
        ],
        "examples": [
            {
                "en": "Click on the settings icon and select account preferences.",
                "tr": "Ayarlar simgesine tıklayın ve hesap tercihlerini seçin.",
                "context": "Software user manual step",
                "register": "neutral_workplace",
                "highlighted_phrase": "Click on"
            },
            {
                "en": "Could you please review this draft before our afternoon meeting?",
                "tr": "Öğleden sonraki toplantımızdan önce bu taslağı inceleyebilir misiniz?",
                "context": "Email to a senior team member",
                "register": "formal_written",
                "highlighted_phrase": "Could you please review"
            },
            {
                "en": "Do not disconnect your device while the update is in progress.",
                "tr": "Güncelleme devam ederken cihazınızın bağlantısını kesmeyin.",
                "context": "System update warning message",
                "register": "formal_written",
                "highlighted_phrase": "Do not disconnect"
            },
            {
                "en": "Would you mind closing the office door on your way out?",
                "tr": "Çıkarken ofis kapısını kapatabilir misiniz acaba?",
                "context": "Speaking to a colleague leaving a quiet room",
                "register": "neutral_workplace",
                "highlighted_phrase": "Would you mind closing"
            }
        ],
        "topic_tags": ["communication", "work-career"]
    },
    {
        "id": "grammar.a2.like-love-hate-gerund",
        "title": "Verbs of Preference: 'Like / Love / Hate + -ing' vs. 'Would like to'",
        "cefr_level": "A2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Verbs expressing general preference (like, love, enjoy, hate) take the gerund (-ing), whereas 'would like / love' expresses a specific desire or polite offer and requires 'to + infinitive'.",
        "summary_tr": "Genel beğenileri bildiren fiiller (like, love, hate, enjoy) fiilimsi (-ing) alırken; 'would like' belirli bir andaki isteği veya kibar bir teklifi ifade eder ve 'to + mastar' gerektirir.",
        "explanation_en": [
            {
                "title": "General Preference vs. Specific Occasion",
                "content": "When talking about hobbies or general enjoyment in life, use verb + -ing: 'I like working from home'. When making a polite request, ordering food, or expressing a present desire, use 'would like to + verb': 'I would like to order the set menu'. Confusing the two leads to misunderstandings about whether you enjoy something in general or want something right now.",
                "patterns": [
                    "General: Subject + like / enjoy / hate + verb-ing (e.g., She loves organizing events)",
                    "Specific desire: Subject + would like / 'd like + to-infinitive (e.g., I would like to ask a question)",
                    "Polite offer: Would you like + to-infinitive / noun? (e.g., Would you like some coffee?)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'severim' ile 'isterim' arasındaki ayrımdır. 'I like swimming' (Yüzmeyi severim - genel hobi) ile 'I would like to swim' (Yüzmek isterim - şu anki arzu) kesinlikle karıştırılmamalıdır. 'Would like' sonrasında fiil her zaman 'to' alır.",
        "rules": [
            {
                "name": "General Preference Gerund",
                "pattern": "Verb of preference + Verb-ing",
                "use_cases": [
                    "Talking about hobbies, leisure activities, and lifestyle habits",
                    "Describing workplace tasks that one finds enjoyable or tedious"
                ]
            },
            {
                "name": "Specific Desire with Would Like",
                "pattern": "Subject + would like ('d like) + to + Base Verb",
                "use_cases": [
                    "Politely ordering at a restaurant or booking travel tickets",
                    "Requesting an appointment or expressing immediate intent in business"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Would Like + Gerund Trap",
                "description_tr": "'Like' fiilinin -ing almasına alışan öğrenciler 'would like' sonrasında da -ing kullanma hatasına düşerler.",
                "trap_example": "I would like having lunch with you today.",
                "correction": "I would like to have lunch with you today.",
                "key_difference_tr": "'Would like' genel beğeni değil tekil bir istek bildirdiği için 'to + mastar' alır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "like + -ing",
                "concept_b": "would like + to",
                "difference_en": "'Like + -ing' denotes timeless enjoyment; 'would like + to' denotes a targeted desire or polite intent.",
                "difference_tr": "'Like + -ing' genel keyif almayı, 'would like + to' ise o anki kibar isteği ifade eder.",
                "example_a": "I like reading technical documentation on weekends.",
                "example_b": "I would like to read the final contract before signing."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "Do you like to drink some tea right now?",
                "correct": "Would you like to drink some tea right now? / Would you like some tea?",
                "explanation_en": "Offers of food or drink use 'Would you like...?', not 'Do you like...?'.",
                "explanation_tr": "İkram ve tekliflerde 'Do you like' değil, 'Would you like' kalıbı kullanılır."
            }
        ],
        "examples": [
            {
                "en": "Many team members enjoy working remotely on Fridays.",
                "tr": "Birçok ekip üyesi cuma günleri uzaktan çalışmaktan keyif alıyor.",
                "context": "Discussing office flexible working policy",
                "register": "neutral_workplace",
                "highlighted_phrase": "enjoy working"
            },
            {
                "en": "I would like to schedule a twenty-minute consultation with your legal team.",
                "tr": "Hukuk ekibinizle yirmi dakikalık bir danışma görüşmesi planlamak isterim.",
                "context": "Professional appointment booking inquiry",
                "register": "formal_written",
                "highlighted_phrase": "would like to schedule"
            },
            {
                "en": "She hates commuting during rush hour because the trains are overcrowded.",
                "tr": "Trenler aşırı kalabalık olduğu için yoğun saatlerde işe gidip gelmekten nefret ediyor.",
                "context": "Talking about daily urban transport routines",
                "register": "neutral_workplace",
                "highlighted_phrase": "hates commuting"
            },
            {
                "en": "Would you like to join us for a brief coffee break?",
                "tr": "Kısa bir kahve molası için bize katılmak ister misiniz?",
                "context": "Colleague inviting another coworker in the hallway",
                "register": "informal_spoken",
                "highlighted_phrase": "Would you like to join"
            }
        ],
        "topic_tags": ["daily-life", "work-career"]
    },
    {
        "id": "grammar.a2.prepositions-of-movement-direction",
        "title": "Prepositions of Movement: 'Into, Out of, Through, Across, Past, and Towards'",
        "cefr_level": "A2",
        "category": "prepositions_and_particles",
        "summary_en": "Prepositions of movement describe dynamic motion toward, away from, or through physical boundaries and spaces.",
        "summary_tr": "Hareket edatları (into, out of, through, across, past, towards) bir mekanın içine, dışına, boyunca veya yönüne doğru gerçekleşen dinamik hareketi belirtir.",
        "explanation_en": [
            {
                "title": "Directional Prepositions with Motion Verbs",
                "content": "Unlike static prepositions of place (in, at, on), prepositions of movement pair with motion verbs (walk, drive, run, fly, enter, pass). 'Into' shows movement entering a 3D container or room; 'out of' indicates exit; 'through' implies moving inside a tunnel, park, or crowd from one side to another; 'across' means moving from one side of a surface or road to the other; 'past' means moving beyond a landmark.",
                "patterns": [
                    "Entry / Exit: go into the meeting room / walk out of the building",
                    "Traversal: walk through the security gate / drive across the bridge",
                    "Direction: walk towards the reception desk / walk past the cafeteria"
                ]
            }
        ],
        "explanation_tr": "Türkçede bu yönelmeler çoğunlukla ismin -e (-a) veya -den (-dan) halleriyle ('odaya', 'köprüden', 'binadan') ifade edilir. İngilizcede ise hareketin türüne göre özel edatlar seçilir: Kapalı alana giriş için 'into', içinden geçmek için 'through', yüzey üzerinde karşıdan karşıya geçmek için 'across' kullanılır.",
        "rules": [
            {
                "name": "Static 'In' vs. Dynamic 'Into'",
                "pattern": "be in (static location) vs. walk/go into (dynamic movement into an enclosed space)",
                "use_cases": [
                    "Giving workplace directions inside office buildings",
                    "Navigating city transit stations, airports, and city streets"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Across vs. Through Confusion",
                "description_tr": "Türkçede her ikisi için de 'karşıya/içinden geçmek' denildiği için park veya orman için 'across', cadde için 'through' kullanma hatası yaygındır.",
                "trap_example": "We walked across the dark tunnel to reach the platform.",
                "correction": "We walked through the dark tunnel to reach the platform.",
                "key_difference_tr": "Tünel, boru veya orman gibi üç boyutlu kapalı alanların 'içinden' geçerken 'through' kullanılır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "in (static location)",
                "concept_b": "into (dynamic movement)",
                "difference_en": "'In' describes being inside; 'into' describes the transition from outside to inside.",
                "difference_tr": "'In' içeride bulunmayı, 'into' ise dışarıdan içeriye doğru hareketi anlatır.",
                "example_a": "The director is sitting in the conference hall.",
                "example_b": "The director walked into the conference hall."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "She went in the office five minutes ago.",
                "correct": "She went into the office five minutes ago.",
                "explanation_en": "Use 'into' rather than 'in' when motion entering an enclosed space is described.",
                "explanation_tr": "Kapalı bir mekana adım atarak giriş hareketinde 'in' yerine 'into' kullanılır."
            }
        ],
        "examples": [
            {
                "en": "Please walk through the security scanner one person at a time.",
                "tr": "Lütfen güvenlik tarayıcısından teker teker geçiniz.",
                "context": "Airport security instruction",
                "register": "neutral_workplace",
                "highlighted_phrase": "through the security scanner"
            },
            {
                "en": "We walked past the main reception and found the elevators on our left.",
                "tr": "Ana danışmanın önünden geçip asansörleri solumuzda bulduk.",
                "context": "Visitor describing navigating a corporate office",
                "register": "neutral_workplace",
                "highlighted_phrase": "past the main reception"
            },
            {
                "en": "The delivery courier stepped out of the elevator holding several parcel boxes.",
                "tr": "Kurye, elinde birkaç koli kutusuyla asansörden dışarı adım attı.",
                "context": "Office morning observation",
                "register": "neutral_workplace",
                "highlighted_phrase": "out of the elevator"
            },
            {
                "en": "You need to walk across the pedestrian crossing to reach the metro station.",
                "tr": "Metro istasyonuna ulaşmak için yaya geçidinden karşıya geçmeniz gerekiyor.",
                "context": "Giving pedestrian directions outside the office",
                "register": "informal_spoken",
                "highlighted_phrase": "across the pedestrian crossing"
            }
        ],
        "topic_tags": ["travel", "daily-life"]
    },
    {
        "id": "grammar.a2.zero-conditional-general-truths",
        "title": "Zero Conditional: General Truths and Cause-Effect Rules",
        "cefr_level": "A2",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "The zero conditional uses 'if / when + present simple' followed by 'present simple' to describe scientific facts, general truths, and consistent operating procedures.",
        "summary_tr": "Sıfır koşul yapısı (Zero Conditional), genel geçer kuralları, doğa kanunlarını ve sistem işleyiş ilkelerini ifade etmek için her iki cümlecikte de geniş zaman (Present Simple) kullanır.",
        "explanation_en": [
            {
                "title": "Cause and Effect Without Future Speculation",
                "content": "Unlike the first conditional which discusses future possibilities, the zero conditional discusses invariable outcomes: if condition X happens, result Y always follows. Because the rule is universal, 'when' can freely replace 'if' without changing the meaning.",
                "patterns": [
                    "If / When + Present Simple, Present Simple (e.g., If water reaches 100 degrees, it boils)",
                    "Present Simple + if / when + Present Simple (e.g., The system restarts when you press reset)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Eğer su 100 dereceye ulaşırsa kaynar' veya 'Butona basınca ekran açılır' yapısıdır. En büyük tuzak, ana cümleye gelecek zaman eki olan 'will' eklemektir. Genel bir kuraldan bahsedildiği için kesinlikle 'will' kullanılmaz; her iki taraf da geniş zaman kalır.",
        "rules": [
            {
                "name": "Zero Conditional Present Symmetry",
                "pattern": "If / When + Subject + Present Simple, Subject + Present Simple",
                "use_cases": [
                    "Explaining software mechanics, device instructions, and technical logic",
                    "Stating company policies, standard procedures, and biological facts"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Will in Zero Conditional Trap",
                "description_tr": "Genel bir sonuç cümlesi kurarken Türkçe geleceğe yönelik hissettiğimiz için ana cümleye 'will' eklemek sıfır koşul yapısını bozar.",
                "trap_example": "If you press this power button for five seconds, the device will restart automatically.",
                "correction": "If you press this power button for five seconds, the device restarts automatically.",
                "key_difference_tr": "Değişmeyen evrensel kurallarda ve cihaz tepkilerinde iki taraf da Present Simple olmalıdır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Zero Conditional (General truth)",
                "concept_b": "First Conditional (Specific future event)",
                "difference_en": "Zero conditional is an absolute rule; first conditional is a prediction about a specific upcoming event.",
                "difference_tr": "Zero conditional genel geçer değişmez prensipleri; First conditional ise gelecekteki olası tekil bir durumu anlatır.",
                "example_a": "If temperature drops below zero, water freezes.",
                "example_b": "If the temperature drops tonight, I will wear a heavier coat."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "When people don't sleep enough, they will feel fatigued.",
                "correct": "When people don't sleep enough, they feel fatigued.",
                "explanation_en": "Universal human biological reactions use Present Simple in both clauses.",
                "explanation_tr": "Biyolojik ve evrensel gerçeklerde sonuç cümlesi 'will' ile değil geniş zamanla kurulur."
            }
        ],
        "examples": [
            {
                "en": "If you click the save button, the application stores your progress locally.",
                "tr": "Kaydet düğmesine tıklarsanız, uygulama ilerlemenizi yerel olarak kaydeder.",
                "context": "Software functionality explanation",
                "register": "neutral_workplace",
                "highlighted_phrase": "stores your progress"
            },
            {
                "en": "When employees work more than eight hours, they receive overtime compensation.",
                "tr": "Çalışanlar sekiz saatten fazla çalıştığında fazla mesai ücreti alırlar.",
                "context": "Company HR policy handbook",
                "register": "formal_written",
                "highlighted_phrase": "receive overtime compensation"
            },
            {
                "en": "If a laptop battery gets too hot, the internal cooling fan turns on automatically.",
                "tr": "Dizüstü bilgisayar bataryası aşırı ısınırsa dahili soğutma fanı otomatik olarak açılır.",
                "context": "Hardware engineering guide",
                "register": "neutral_workplace",
                "highlighted_phrase": "turns on automatically"
            },
            {
                "en": "Plants die quickly if they do not receive sufficient sunlight and water.",
                "tr": "Bitkiler yeterli güneş ışığı ve su almazlarsa hızla ölürler.",
                "context": "Basic biological science explanation",
                "register": "neutral_workplace",
                "highlighted_phrase": "die quickly if"
            }
        ],
        "topic_tags": ["science", "technology", "daily-life"]
    }
]
