"""P8-004 Grammar micro-lessons A2-B2 focused on Turkish L1 transfer traps."""

GRAMMAR_A2_B2 = [
    # ==================== A2 (6 lessons) ====================
    {
        "id": "grammar.a2.null-subject-dummy-it-there",
        "title": "Dummy 'It' and 'There' vs. Null-Subject Omission",
        "cefr_level": "A2",
        "category": "noun_phrases_and_articles",
        "summary_en": "English requires an explicit grammatical subject ('it' or 'there') even when Turkish leaves the subject implied in verb morphology.",
        "summary_tr": "İngilizce, Türkçedeki gizli öznenin aksine iklim, zaman, mesafe ve genel yargılarda 'it' veya 'there' öznesini zorunlu kılar.",
        "explanation_en": [
            {
                "title": "Why English Uses Empty Placeholder Subjects",
                "content": "Every finite declarative sentence in standard English must fill the subject slot before the verb. When referring to weather, time, distance, or an infinitive evaluation, English inserts dummy 'it' or existential 'there'.",
                "patterns": [
                    "It + be + adjective + to-infinitive / that-clause",
                    "There + be + noun phrase + place/time",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Gerekli' veya 'Yağmur yağıyor' cümlelerinde ayrı bir zamir gerekmez; oysa İngilizcede özne yeri boş bırakılamaz. Değerlendirme sıfatlarında 'It is...', varlık bildirmede 'There is/are...' kullanılır.",
        "rules": [
            {
                "name": "Anticipatory 'It' for Evaluations",
                "pattern": "It + is/was + Adjective + to + Verb",
                "use_cases": ["Stating feasibility, necessity, or difficulty without dropping the subject"],
                "time_markers": ["always", "today", "before release"],
            },
            {
                "name": "Existential 'There' for New Information",
                "pattern": "There + is/are + Singular/Plural Noun + Location",
                "use_cases": ["Introducing the presence of problems, files, or schedules"],
                "time_markers": ["now", "this morning", "on the agenda"],
            },
        ],
        "contrasts": [
            {
                "structure_a": "It is helpful to review daily.",
                "structure_b": "There is a helpful review session today.",
                "difference_explanation_en": "'It' anticipates an infinitive action; 'There' introduces the existence of a noun phrase.",
                "difference_explanation_tr": "'It' arkasından gelen eylemi (to review) işaret eder; 'There' ise bir şeyin varlığını bildirir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Gizli Özne Nedeniyle 'It' Zamirini Atlamak",
                "explanation_tr": "Türkçede 'Önemli' veya 'Doğru değil' yapılarında özne gizlidir. Bunu birebir çevirmek fiili öznesiz bırakır ve dilbilgisi hatası doğurur.",
                "incorrect_example": "Is critical to back up local files before updating.",
                "correct_example": "It is critical to back up local files before updating.",
            }
        ],
        "examples": [
            {
                "en": "It is essential to confirm the meeting link fifteen minutes early.",
                "tr": "Toplantı bağlantısını on beş dakika önceden doğrulamak şarttır.",
                "rule_highlight": "It is essential to confirm",
                "context": "workplace coordination",
            },
            {
                "en": "There are three unread client tickets waiting in the regional queue.",
                "tr": "Bölge kuyruğunda bekleyen üç okunmamış müşteri talebi var.",
                "rule_highlight": "There are three unread client tickets",
                "context": "support operations",
            },
        ],
        "topic_tags": ["grammar", "syntax", "turkish_l1_traps", "workplace"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.a2.definite-zero-article-basics",
        "title": "Definite 'The' vs. Zero Article in Everyday Workplaces",
        "cefr_level": "A2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Use 'the' when both speaker and listener identify the specific item, and omit articles with general plural/uncountable concepts and routine institutions.",
        "summary_tr": "Belirli ve bilinen nesnelerde 'the' kullanılırken, genel kavramlarda ve 'work / school / lunch' gibi rutin ifadelerde artikel kullanılmaz.",
        "explanation_en": [
            {
                "title": "Specific Reference vs. Institutional Routine",
                "content": "English distinguishes 'the office down the hall' (a concrete building) from 'go to work' (daily occupational activity without 'the'). General uncountable terms like 'feedback' or 'traffic' also take zero article unless narrowed by context.",
                "patterns": [
                    "go to work / at work / at home (zero article)",
                    "open the report / check the schedule (shared specific referent)",
                ],
            }
        ],
        "explanation_tr": "Türkçede belirli/belirsiz tanımlık sistemi aynı şekilde işlemediği için 'the work' veya 'the traffic in general' gibi fazla 'the' kullanımı sık görülür. Genel kavramlar ve rutin yer bildirimleri sıfır artikelle kurulur.",
        "rules": [
            {
                "name": "Zero Article for General Uncountables",
                "pattern": "Uncountable Noun (without 'the') + Verb",
                "use_cases": ["Talking about advice, data, time, or coffee in general"],
                "time_markers": ["usually", "generally", "often"],
            },
            {
                "name": "Definite Article for Shared Context",
                "pattern": "the + Noun (+ identifying modifier)",
                "use_cases": ["Referring to a document, room, or file already clear in context"],
                "time_markers": ["this morning", "now"],
            },
        ],
        "contrasts": [
            {
                "structure_a": "Good feedback helps teams improve.",
                "structure_b": "The feedback from yesterday's client call was positive.",
                "difference_explanation_en": "General concepts omit 'the'; narrowed, specific instances require 'the'.",
                "difference_explanation_tr": "Genel anlamdaki 'geri bildirim' artikel almaz; belirli bir görüşmenin geri bildirimi ise 'the' ister.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "Genel İsimlerin ve 'Work' Kelimesinin Önüne Fazla 'The' Koymak",
                "explanation_tr": "'İşe gidiyorum' veya 'Genelde trafik yoğun' cümlelerinde Türkçeden çevirirken gereksiz 'the' eklemek doğallığı bozar ('go to the work' yerine 'go to work').",
                "incorrect_example": "I arrive at the work early when the traffic is light.",
                "correct_example": "I arrive at work early when traffic is light.",
            }
        ],
        "examples": [
            {
                "en": "She stayed at work until six to finish the quarterly expense sheet.",
                "tr": "Üç aylık harcama tablosunu bitirmek için altıya kadar işte kaldı.",
                "rule_highlight": "at work ... the quarterly expense sheet",
                "context": "daily office routine",
            },
            {
                "en": "Clear documentation saves time during employee onboarding.",
                "tr": "Net dokümantasyon çalışan oryantasyonunda zaman kazandırır.",
                "rule_highlight": "Clear documentation saves time",
                "context": "workplace training",
            },
        ],
        "topic_tags": ["articles", "workplace", "daily-life"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.a2.prepositions-movement-direction",
        "title": "Prepositions of Movement vs. Static Location: To, Into, Onto",
        "cefr_level": "A2",
        "category": "prepositions_and_particles",
        "summary_en": "Separate dynamic directional prepositions ('to', 'into', 'onto', 'toward') from static position prepositions ('at', 'in', 'on').",
        "summary_tr": "Türkçedeki '-e/-a' ve '-de/-da' eklerini tek prepozisyona indirgemeden hareket bildiren 'to / into / onto' ile konum bildiren 'at / in / on' edatlarını ayırın.",
        "explanation_en": [
            {
                "title": "Destination vs. Container Entry",
                "content": "Use 'to' for direction toward a target ('go to Ankara', 'send to the client'). Use 'into' when crossing inside a physical or digital boundary ('import into the folder') and keep 'in/at' for resting position.",
                "patterns": [
                    "move / go / fly + to + Destination (except 'home')",
                    "upload / walk / enter -> import into / put onto",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'ofise gitmek' ile 'dosyayı klasöre atmak' benzer yönelme eki alsa da İngilizcede hedef nokta 'to', içine giriş 'into', yüzeye koyma 'onto' ile anlatılır.",
        "rules": [
            {
                "name": "Direction vs. Arrival",
                "pattern": "go/travel + to + Place (exception: go home; arrive in/at)",
                "use_cases": ["Describing trips, transfers, and sending items to recipients"],
                "time_markers": ["this afternoon", "next Monday"],
            },
            {
                "name": "Boundary Crossing ('Into' & 'Onto')",
                "pattern": "put/copy/step + into/onto + Container/Surface",
                "use_cases": ["Entering rooms or moving files into folders/platforms"],
                "time_markers": ["before noon"],
            },
        ],
        "contrasts": [
            {
                "structure_a": "She walked in the office for twenty minutes.",
                "structure_b": "She walked into the office at nine.",
                "difference_explanation_en": "'in' describes movement inside already; 'into' shows entering from outside.",
                "difference_explanation_tr": "'in' içeride yürümeyi, 'into' ise dışarıdan içeri girmeyi anlatır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Arrive', 'Enter' ve 'Go Home' Sonrası Fazla 'To' Eklemek",
                "explanation_tr": "'Eve gitmek', 'ofise varmak' gibi yapılarda Türkçe '-e' eki nedeniyle 'go to home' veya 'arrive to Ankara' hatası yapılır; 'go home', 'arrive in/at' ve 'enter + noun' edat almaz.",
                "incorrect_example": "When I arrived to the airport, I went to home quickly after customs.",
                "correct_example": "When I arrived at the airport, I went home quickly after customs.",
            }
        ],
        "examples": [
            {
                "en": "Please copy these invoices into the shared accounting folder before Friday.",
                "tr": "Lütfen bu faturaları cumadan önce ortak muhasebe klasörüne kopyalayın.",
                "rule_highlight": "copy ... into",
                "context": "office admin",
            },
            {
                "en": "We flew to Izmir on Tuesday morning and arrived at the venue on time.",
                "tr": "Salı sabahı İzmir'e uçtuk ve mekana tam vaktinde vardık.",
                "rule_highlight": "flew to ... arrived at",
                "context": "business travel",
            },
        ],
        "topic_tags": ["prepositions", "travel", "workplace"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.a2.word-order-svo-place-time",
        "title": "English SVO + Place-Time Word Order vs. Turkish SOV",
        "cefr_level": "A2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Keep Verb and Direct Object side by side, placing Manner, Place, and Time adverbs cleanly after the object.",
        "summary_tr": "İngilizcede fiil ile nesneyi ayırmadan, durum-yer-zaman zarflarını cümle sonuna sıralama (SVO-MPT) kuralını uygulayın.",
        "explanation_en": [
            {
                "title": "Do Not Split Verb and Direct Object",
                "content": "Turkish permits placing time or location between subject, object, and verb. English almost never places location or time expressions between the verb and its object: write 'Verb + Object + Place + Time'.",
                "patterns": [
                    "Subject + Verb + Object + Manner + Place + Time",
                    "Time + , + Subject + Verb + Object + Place (optional fronted time)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Raporu ofiste dün yazdım' dizimi doğaldır. Bunu İngilizceye aktarırken zaman veya yeri fiil ile nesne arasına sokmak ('wrote yesterday the report') hatalı dizime yol açar.",
        "rules": [
            {
                "name": "Standard SVO + MPT Ordering",
                "pattern": "Subject + Verb + Object + Place + Time",
                "use_cases": ["Building clean, natural statements about daily tasks and travel"],
                "time_markers": ["yesterday", "every Monday", "in the morning"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Yesterday we reviewed the draft in Ankara.",
                "structure_b": "We reviewed the draft in Ankara yesterday.",
                "difference_explanation_en": "Time may appear at the very front or at the end, but never between verb and object.",
                "difference_explanation_tr": "Zaman zarfı en başa ya da en sona gelebilir ancak asla fiil ile nesnenin arasına girmez.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Fiil ile Nesne Arasına Yer veya Zaman Zarfı Koymak",
                "explanation_tr": "Türkçe söz dizimi etkisinden dolayı 'read every day emails' veya 'sent yesterday the file' kurmak çok yaygındır; nesneyi daima fiilin hemen yanına getirin.",
                "incorrect_example": "We checked yesterday in the office the quarterly totals.",
                "correct_example": "We checked the quarterly totals in the office yesterday.",
            }
        ],
        "examples": [
            {
                "en": "I study business vocabulary quietly on the tablet every evening.",
                "tr": "Her akşam tablette sessizce iş İngilizcesi çalışırım.",
                "rule_highlight": "study business vocabulary quietly on the tablet every evening",
                "context": "daily routine",
            },
            {
                "en": "The trainer demonstrated the new workflow clearly at our Istanbul branch last week.",
                "tr": "Eğitmen geçen hafta İstanbul şubemizde yeni iş akışını açıkça gösterdi.",
                "rule_highlight": "demonstrated the new workflow clearly at our Istanbul branch last week",
                "context": "training",
            },
        ],
        "topic_tags": ["word_order", "daily-life", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.a2.indefinite-pronouns-some-any-no",
        "title": "Indefinite Pronouns and Single Negation: Someone, Anyone, No One",
        "cefr_level": "A2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Avoid double negation by pairing 'no one / nothing / nowhere' with positive verbs or 'anyone / anything' with negative verbs.",
        "summary_tr": "'Hiç kimse ...madı' kalıbındaki çift olumsuzluğu İngilizceye taşımayın: olumsuz fiille 'anyone', olumlu fiille 'no one' kullanın.",
        "explanation_en": [
            {
                "title": "One Negative Element per Clause",
                "content": "Standard English expresses negation once per clause. Say 'Nobody called' OR 'Anyone didn't call? Wait—Nobody called / We didn't call anyone'. Also remember that indefinite pronouns like 'everyone' and 'nothing' take singular verbs.",
                "patterns": [
                    "Positive verb + no one / nothing / nowhere",
                    "Negative verb (didn't / haven't) + anyone / anything / anywhere",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Kimse aramadı' ve 'Hiçbir şey görmedim' cümlelerinde iki olumsuzluk yan yana gelir. İngilizcede tek olumsuzluk kuralı vardır: ya fiili olumsuz yapıp 'any-' seçin ya da olumlu fiille 'no-' kullanın.",
        "rules": [
            {
                "name": "Negative Compounds vs. Any- Compounds",
                "pattern": "Nobody + Verb(positive) == Subject + didn't + Verb + anybody",
                "use_cases": ["Reporting absences, zero errors, or open offers politely"],
                "time_markers": ["yet", "so far", "today"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Everyone in operations has access.",
                "structure_b": "Did anyone check the backup log?",
                "difference_explanation_en": "'Everyone' takes singular verb agreement ('has'); 'anyone' is standard in neutral questions.",
                "difference_explanation_tr": "'Everyone' tekil fiil alır ('has'); sorularda genelde 'anyone' kullanılır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Türkçe Çift Olumsuzluk Kalıbını Aktarmak",
                "explanation_tr": "'Hiç kimse gelmedi' veya 'Hiçbir şey bulamadık' cümlelerini 'Nobody didn't come' şeklinde kurmak temel bir aktarım hatasıdır.",
                "incorrect_example": "Nobody didn't mention the pricing update in the chat.",
                "correct_example": "Nobody mentioned the pricing update in the chat.",
            }
        ],
        "examples": [
            {
                "en": "We did not notice anything unusual during the morning health check.",
                "tr": "Sabah durum kontrolü sırasında olağandışı hiçbir şey fark etmedik.",
                "rule_highlight": "did not notice anything",
                "context": "operations",
            },
            {
                "en": "Nothing guarantees smooth launches like testing edge cases twice.",
                "tr": "Hiçbir şey sorunsuz lansmanı uç senaryoları iki kez test etmek kadar garanti edemez.",
                "rule_highlight": "Nothing guarantees",
                "context": "engineering",
            },
        ],
        "topic_tags": ["pronouns", "communication", "negation"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.a2.say-tell-speak-talk",
        "title": "Speech Verbs in Action: Say, Tell, Speak, and Talk",
        "cefr_level": "A2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Choose the right valency pattern: 'tell someone something' vs. 'say something (to someone)' and 'speak/talk with/about'.",
        "summary_tr": "'Tell' doğrudan muhatap nesnesi isterken 'say' söyleneni önceler ve alıcıya 'to' ile bağlanır; dil veya konu için 'speak/talk' seçilir.",
        "explanation_en": [
            {
                "title": "Valency Differences Between Everyday Speech Verbs",
                "content": "While 'say' and 'tell' share meaning, their grammar diverges sharply. Never drop 'to' after 'say' before a person, and never insert 'to' right after 'tell'.",
                "patterns": [
                    "tell + listener + info / to-infinitive",
                    "say + words / (that)... + [to listener]",
                    "speak + language / speak with someone about topic",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'bana söyledi / bana dedi' yapıları aynı kurguyla işlediği için İngilizcede '*said me' veya '*told to me' hataları çok sıktır. 'Tell me' ve 'say to me' ayrımını mekanikleştirin.",
        "rules": [
            {
                "name": "Object Placement with Say vs. Tell",
                "pattern": "tell + Person + Clause | say + Clause (to + Person)",
                "use_cases": ["Relaying instructions, updates, and client quotes cleanly"],
                "time_markers": ["earlier", "just now"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "She told us to wait.",
                "structure_b": "She said that we should wait.",
                "difference_explanation_en": "'Tell' needs a listener object before the infinitive; 'say' introduces speech directly.",
                "difference_explanation_tr": "'Tell' hemen ardından dinleyiciyi alır; 'say' doğrudan söylenen söze geçer.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Say Me' ve 'Tell To Me' Edat Karışıklığı",
                "explanation_tr": "'Bana dedi' kalıbı 'told to me' ya da 'said me' olarak yanlış aktarılır. Doğruları yalnızca 'told me' ve 'said to me' şeklindedir.",
                "incorrect_example": "The director said me to update the timeline and told to us the reason.",
                "correct_example": "The director told me to update the timeline and explained the reason to us.",
            }
        ],
        "examples": [
            {
                "en": "Can you tell the support lead that offline sync is passing all tests?",
                "tr": "Destek liderine çevrimdışı eşitlemenin tüm testleri geçtiğini söyleyebilir misin?",
                "rule_highlight": "tell the support lead that",
                "context": "team chat",
            },
            {
                "en": "She speaks Turkish and German fluently when negotiating with suppliers.",
                "tr": "Tedarikçilerle müzakere ederken Türkçe ve Almancayı akıcı konuşur.",
                "rule_highlight": "speaks Turkish and German fluently",
                "context": "workplace profile",
            },
        ],
        "topic_tags": ["communication", "verb_patterns", "workplace"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    # ==================== B1 (8 lessons) ====================
    {
        "id": "grammar.b1.since-for-during-ago",
        "title": "Duration and Time Anchors: Since, For, During, and Ago",
        "cefr_level": "B1",
        "category": "tenses_and_aspect",
        "summary_en": "Separate duration length ('for'), starting point ('since'), named period ('during'), and past countback ('ago') cleanly.",
        "summary_tr": "'For' (süre uzunluğu), 'since' (başlangıç noktası), 'during' (belirli dönem içi) ve 'ago' (tamamlanmış geçmiş) zaman bağlaçlarını doğru zamanlarla eşleştirin.",
        "explanation_en": [
            {
                "title": "Anchored Time vs. Elapsed Duration",
                "content": "'For' measures how long something lasts ('for three hours'); 'since' marks when an ongoing state started ('since Monday'); 'during' sits before a noun event ('during the flight'); and 'ago' pairs strictly with Past Simple.",
                "patterns": [
                    "have/has + V3 / been V-ing + for [duration] / since [start point]",
                    "during + [noun period] | Past Simple + [duration] + ago",
                ],
            }
        ],
        "explanation_tr": "Türkçede '3 yıldır çalışıyorum' ve '3 yıl önce başladım' benzer kelimelerle kurulsa da İngilizcede 'since' arkasına sayısal süre almaz ('*since 3 years'), 'during' cümle almaz ve 'ago' Present Perfect ile yan yana gelmez.",
        "rules": [
            {
                "name": "Present Perfect Duration Pairing",
                "pattern": "Present Perfect (+ Continuous) + for [length] / since [starting date/clause]",
                "use_cases": ["Explaining tenure, ongoing outages, or project history"],
                "time_markers": ["since Q1", "for six months", "recently"],
            },
            {
                "name": "Event-Contained 'During' vs. Completed 'Ago'",
                "pattern": "during + the workshop / summit | [Past Simple] + two weeks + ago",
                "use_cases": ["Pinpointing when a completed past step occurred"],
                "time_markers": ["ago", "during last sprint"],
            },
        ],
        "contrasts": [
            {
                "structure_a": "We have maintained offline sync for two years.",
                "structure_b": "We launched offline sync two years ago.",
                "difference_explanation_en": "'For' + Perfect covers continuing span up to now; 'ago' + Past Simple marks a finished past point.",
                "difference_explanation_tr": "'For' bugüne süren zamanı, 'ago' ise geçmişte kalıp bitmiş başlangıç anını belirtir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "'Since' İle Uzunluk Kullanmak ve 'Ago' İle Perfect Kurmak",
                "explanation_tr": "'İki yıldan beri' ifadesini '*since two years' ya da '*I have joined three years ago' diye kurmak Türkçe öğrencilerin en sık yaptığı zaman aktarım hatasıdır.",
                "incorrect_example": "I have joined this company since four years ago.",
                "correct_example": "I joined this company four years ago and have worked here for four years.",
            }
        ],
        "examples": [
            {
                "en": "No database alerts have fired since we upgraded the validation script on Tuesday.",
                "tr": "Salı günü doğrulama betiğini güncellediğimizden beri hiçbir veritabanı uyarısı tetiklenmedi.",
                "rule_highlight": "have fired since we upgraded",
                "context": "engineering review",
            },
            {
                "en": "Learners revised thirty collocations offline during the flight to Frankfurt.",
                "tr": "Öğrenciler Frankfurt uçuşu sırasında çevrimdışı olarak otuz eşdizimlilik tekrar etti.",
                "rule_highlight": "during the flight",
                "context": "business travel",
            },
        ],
        "topic_tags": ["tenses", "time", "workplace"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.embedded-indirect-questions",
        "title": "Indirect and Embedded Questions in Polite Work Requests",
        "cefr_level": "B1",
        "category": "reported_speech",
        "summary_en": "Keep Subject + Verb order inside embedded questions ('Could you tell me when the audit starts?') without repeating 'do/did' inversion.",
        "summary_tr": "Kibar soru kalıplarının içine yerleşen sorularda tekrar yardımcı fiil devirmesi yapmayıp düz cümle sırasını (Özne + Yüklem) koruyun.",
        "explanation_en": [
            {
                "title": "Inversion Happens Only Once",
                "content": "In 'Could you confirm where the contract is?', only the outer frame ('Could you') inverts. The embedded clause reverts strictly to affirmative Subject + Verb order, dropping dummy 'do/does/did'.",
                "patterns": [
                    "Could you tell me + wh-word + Subject + Verb...?",
                    "I was wondering whether + Subject + Verb...",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Nerede olduğunu sorabilir miyim?' tek bir soru bütünüdür. İngilizcede ise 'Do you know where is the file?' demek çifte soru devirmesi hatasıdır; doğrusu 'where the file is' dizimidir.",
        "rules": [
            {
                "name": "Embedded Statement Syntax",
                "pattern": "Polite Opener + wh-word / if / whether + Subject + Verb",
                "use_cases": ["Softening direct questions in client emails and cross-team meetings"],
                "time_markers": ["when convenient", "today"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "When does finance close the ledger?",
                "structure_b": "Could you let me know when finance closes the ledger?",
                "difference_explanation_en": "Direct questions invert with 'does'; embedded polite questions restore regular verb conjugation.",
                "difference_explanation_tr": "Doğrudan soruda 'does' ile devirme yapılır; kibar gömülü soruda 'closes' düz haline döner.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Kibar Soru İçinde Yardımcı Fiili İkinci Kez Devirmek",
                "explanation_tr": "Kibar girişin ardına 'where is he' veya 'why did they leave' eklemek yerine 'where he is' ve 'why they left' düz sırasını korumalısınız.",
                "incorrect_example": "Could you clarify why did the build script fail yesterday?",
                "correct_example": "Could you clarify why the build script failed yesterday?",
            }
        ],
        "examples": [
            {
                "en": "I was wondering whether the vendor sent the signed SLA yet.",
                "tr": "Tedarikçinin imzalı SLA'yı henüz gönderip göndermediğini merak ediyordum.",
                "rule_highlight": "whether the vendor sent",
                "context": "client email",
            },
            {
                "en": "Do you happen to know which branch currently owns that migration?",
                "tr": "Şu anda o geçişin hangi dalda yürütüldüğünü biliyor musunuz acaba?",
                "rule_highlight": "which branch currently owns",
                "context": "team standup",
            },
        ],
        "topic_tags": ["questions", "emails", "register"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.zero-article-generics-abstract",
        "title": "Zero Article vs. Definite 'The' in Abstract and Plural Generics",
        "cefr_level": "B1",
        "category": "noun_phrases_and_articles",
        "summary_en": "Omit 'the' when making general statements about plural nouns or abstract uncountables ('Metrics guide decisions'), and add 'the' only for defined subsets.",
        "summary_tr": "Çoğul genel ifadelerde ve soyut kavramlarda genelleme yaparken 'the' kullanmayın; yalnızca belirli bir alt kümeyi daralttığınızda 'the' ekleyin.",
        "explanation_en": [
            {
                "title": "Generic Truths vs. Narrowed Reference",
                "content": "Abstract nouns (trust, software, advice, compliance) and plural categories (managers, budgets) take zero article when speaking generally. Add 'the' as soon as a prepositional phrase or clause pins down which subset you mean.",
                "patterns": [
                    "Plural/Uncountable Noun + Verb (general truth)",
                    "the + Noun + of / that-clause (specified scope)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Müşteriler hızı sever' ile 'Dünkü toplantıdaki müşteriler' biçimsel olarak aynı isim kalıbıyla başlar. İngilizcede ilki 'Clients value speed', ikincisi ise 'The clients from yesterday's call' biçiminde ayrılır.",
        "rules": [
            {
                "name": "Zero Article for Generalizations",
                "pattern": "Abstract/Plural Subject + Present Verb",
                "use_cases": ["Formulating business principles, policies, and report generalizations"],
                "time_markers": ["in general", "typically"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Privacy builds user trust.",
                "structure_b": "The privacy rules approved last quarter apply globally.",
                "difference_explanation_en": "Broad 'privacy' has no article; qualified 'privacy rules approved last quarter' takes 'the'.",
                "difference_explanation_tr": "Genel olarak 'mahremiyet' artikel almaz; belirli çeyrekte onaylanan kurallar 'the' alır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "null_subject_transfer",
                "trap_title": "Genel İfadelerde 'The + Çoğul/Soyut İsim' Hatası",
                "explanation_tr": "Soyut veya genel çoğul öznelere 'the' eklemek ('The software engineers need autonomy') cümleyi sanki odadaki belli bir ekipten bahsediyormuş gibi yanlış daraltır.",
                "incorrect_example": "The communication and the patience are essential for the managers.",
                "correct_example": "Communication and patience are essential for managers.",
            }
        ],
        "examples": [
            {
                "en": "Clear naming conventions save engineers hours during debugging.",
                "tr": "Net adlandırma kuralları mühendislere hata ayıklamada saatler kazandırır.",
                "rule_highlight": "Clear naming conventions save engineers",
                "context": "engineering",
            },
            {
                "en": "The naming conventions documented in AGENTS.md apply to every batch.",
                "tr": "AGENTS.md içinde belgelenen adlandırma kuralları her pakete uygulanır.",
                "rule_highlight": "The naming conventions documented in",
                "context": "governance",
            },
        ],
        "topic_tags": ["articles", "writing", "academic"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.verb-preposition-direct-traps",
        "title": "Transitive Verbs Without Prepositions vs. Mandatory Verb-Preposition Pairs",
        "cefr_level": "B1",
        "category": "prepositions_and_particles",
        "summary_en": "Avoid adding extra prepositions after direct transitive verbs like 'discuss' or 'enter', while keeping mandatory partners on 'depend on', 'listen to', and 'explain to'.",
        "summary_tr": "'Discuss / mention / enter' gibi fiiller doğrudan nesne alırken, 'depend on / explain to / agree with' gibi fiillerin zorunlu edatlarını düşürmeyin.",
        "explanation_en": [
            {
                "title": "Direct Objects vs. Prepositional Bridges",
                "content": "Because Turkish case suffixes ('-i', '-e', '-le') do not map one-to-one to English prepositions, learners frequently swap them: inserting 'about' after 'discuss' while omitting 'to' in 'explain to me'.",
                "patterns": [
                    "discuss / enter / emphasize / attend + Object (NO preposition)",
                    "explain [something] + to [listener] | depend on | insist on",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'konuyu tartışmak' yerine 'hakkında tartışmak' gibi düşünülerek '*discuss about', ya da 'bana açıkla' diye düşünülerek '*explain me' hatası yapılır.",
        "rules": [
            {
                "name": "Direct Transitive Verbs (Zero Preposition)",
                "pattern": "discuss / approach / mention / reach + Object",
                "use_cases": ["Running crisp meetings and written status briefs"],
                "time_markers": ["in today's sync", "later"],
            },
            {
                "name": "'Explain Something to Someone'",
                "pattern": "explain + Topic + to + Listener",
                "use_cases": ["Clarifying complex fixes to clients or teammates"],
                "time_markers": ["briefly"],
            },
        ],
        "contrasts": [
            {
                "structure_a": "Let us discuss the Q3 roadmap.",
                "structure_b": "Let us talk about the Q3 roadmap.",
                "difference_explanation_en": "'Discuss' is direct transitive; only 'talk' or 'speak' uses 'about'.",
                "difference_explanation_tr": "'Discuss' doğrudan nesne alır; 'about' yalnızca 'talk' veya 'speak' ile gelir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Discuss About' Eklemesi ve 'Explain Me' Eksiltmesi",
                "explanation_tr": "'Bana anlat' kalıbından kaynaklanan '*explain me' ve 'hakkında tartışmak'tan doğan '*discuss about' kurumsal konuşmalarda hemen göze çarpan klasik B1 tuzaklarıdır.",
                "incorrect_example": "Please explain me this chart before we discuss about the pricing.",
                "correct_example": "Please explain this chart to me before we discuss the pricing.",
            }
        ],
        "examples": [
            {
                "en": "Whether we ship Friday depends on final validation counts.",
                "tr": "Cuma günü çıkış yapıp yapmayacağımız nihai doğrulama sayılarına bağlıdır.",
                "rule_highlight": "depends on final validation counts",
                "context": "release planning",
            },
            {
                "en": "Every contractor must attend the security briefing before receiving access.",
                "tr": "Her yüklenici erişim almadan önce güvenlik brifingine katılmalıdır.",
                "rule_highlight": "attend the security briefing",
                "context": "onboarding",
            },
        ],
        "topic_tags": ["prepositions", "meetings", "turkish_l1_traps"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.phrasal-verb-particle-separability",
        "title": "Separable vs. Inseparable Phrasal Verbs & Pronoun Position",
        "cefr_level": "B1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Place object pronouns strictly between verb and particle for separable phrasal verbs ('follow it up'), while keeping full nouns flexible.",
        "summary_tr": "Ayrılabilir öbek fiillerde zamir nesneyi ('it / them') mutlaka fiil ile edat arasına koyun ('turn it off'); tam isimlerde her iki yer de geçerlidir.",
        "explanation_en": [
            {
                "title": "Why Pronouns Cannot Trail Separable Particles",
                "content": "With separable transitive phrasal verbs ('bring up', 'sort out', 'hand over'), noun phrases may sit in the middle or at the end, yet short pronouns MUST sit inside: 'write it down', never '*write down it'. Inseparable verbs like 'look into' keep objects after the particle.",
                "patterns": [
                    "Separable: Verb + Pronoun + Particle ('sort it out')",
                    "Inseparable: Verb + Particle + Noun/Pronoun ('look into it')",
                ],
            }
        ],
        "explanation_tr": "Türkçede ayrı fiil+edat ayrışması olmadığı için öğrenciler 'pick up it' veya 'call off it' sırasını kullanır. Kısa zamirler ('it, them, her') ayrılabilir fiillerde kesinlikle ortaya yerleşir.",
        "rules": [
            {
                "name": "Middle Placement for Pronoun Objects",
                "pattern": "Verb + it/them/me + off/up/out/over/down",
                "use_cases": ["Referring back to tickets, calls, or tasks naturally in conversation"],
                "time_markers": ["right away", "before 5 p.m."],
            }
        ],
        "contrasts": [
            {
                "structure_a": "We will sort it out before launch.",
                "structure_b": "We will look into it before launch.",
                "difference_explanation_en": "'Sort out' is separable (pronoun in middle); 'look into' is prepositional inseparable (pronoun after).",
                "difference_explanation_tr": "'Sort out' ayrılabilir olduğu için zamiri ortaya alır; 'look into' ayrılmazdır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Ayrılabilir Öbek Fiillerde Zamiri Sona Atmak ('*Write Down It')",
                "explanation_tr": "'Dosyayı doldur' için 'fill in the form' doğruyken 'onu doldur' derken '*fill in it' demek yanlıştır; doğrusu 'fill it in' olmalıdır.",
                "incorrect_example": "Here is the customs form; please fill out it before boarding.",
                "correct_example": "Here is the customs form; please fill it out before boarding.",
            }
        ],
        "examples": [
            {
                "en": "I noticed a minor typo on slide three and fixed it up right away.",
                "tr": "Üçüncü slaytta ufak bir yazım hatası fark edip hemen düzelttim.",
                "rule_highlight": "fixed it up",
                "context": "team presentation",
            },
            {
                "en": "Can you bring that pricing concern up during tomorrow's vendor call?",
                "tr": "O fiyatlandırma endişesini yarınki tedarikçi görüşmesinde gündeme getirebilir misin?",
                "rule_highlight": "bring that pricing concern up",
                "context": "meetings",
            },
        ],
        "topic_tags": ["phrasal_verbs", "speaking", "word_order"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.if-vs-whether-uncertainty",
        "title": "Indirect Choices and Doubt: If vs. Whether in Work Clauses",
        "cefr_level": "B1",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "Use 'whether' instead of 'if' after prepositions, before infinitives, as a sentence subject, or directly before 'or not'.",
        "summary_tr": "Dolaylı belirsizliklerde edatlardan sonra, mastarlardan önce ve doğrudan 'or not' ile birlikte 'if' yerine 'whether' tercih edin.",
        "explanation_en": [
            {
                "title": "Where 'Whether' Is Mandatory and Cleaner",
                "content": "In casual speech after 'know' or 'ask', 'if' and 'whether' often overlap. However, formal writing and structural grammar enforce 'whether' right after prepositions ('depends on whether'), before 'to + verb' ('decide whether to ship'), and alongside 'whether or not'.",
                "patterns": [
                    "Preposition + whether + Subject + Verb",
                    "decide / discuss + whether to + Verb",
                    "whether or not + Clause (contrastive concession)",
                ],
            }
        ],
        "explanation_tr": "Türkçede '-ip -mediği' anlamını tek kalıp verdiği için tüm bağlamlara 'if' yerleştirme eğilimi vardır. 'Depends on if...' veya 'wonder if to go' hatalıdır; her ikisinde 'whether' gereklidir.",
        "rules": [
            {
                "name": "Whether After Prepositions & Infinitives",
                "pattern": "Preposition + whether + Clause | Verb + whether + to-Infinitive",
                "use_cases": ["Expressing conditional scope, procurement choices, and architectural decisions"],
                "time_markers": ["before deployment", "this quarter"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Please ask if she is available.",
                "structure_b": "Our decision depends on whether capacity holds.",
                "difference_explanation_en": "'Ask if' is acceptable in simple indirect yes/no questions; after prepositions ('on'), only 'whether' works.",
                "difference_explanation_tr": "Basit sorma cümlelerinde 'if' uygunken edattan ('on') sonra yalnızca 'whether' gelebilir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "Edat veya Mastar Önünde 'If' Kullanmak",
                "explanation_tr": "Türkçe '-ip -mediği' ekini otomatik olarak 'if' ile eşleştirmek '*depends on if we pass' gibi hatalı cümlelere yol açar; edat sonrası daima 'whether' kullanın.",
                "incorrect_example": "Approval depends on if legal decides if to add a grace period.",
                "correct_example": "Approval depends on whether legal decides to add a grace period.",
            }
        ],
        "examples": [
            {
                "en": "Engineers are still evaluating whether to compress local audio or split downloads.",
                "tr": "Mühendisler hâlâ yerel sesi sıkıştırmak ya da indirmeleri bölmek konusunda karar değerlendiriyor.",
                "rule_highlight": "evaluating whether to compress",
                "context": "architecture decision",
            },
            {
                "en": "The offline validator runs deterministically whether or not Wi-Fi is active.",
                "tr": "Çevrimdışı doğrulayıcı Wi-Fi açık olsa da olmasa da deterministik biçimde çalışır.",
                "rule_highlight": "whether or not",
                "context": "reliability spec",
            },
        ],
        "topic_tags": ["conditionals", "decision_making", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.stative-vs-dynamic-verbs",
        "title": "Stative vs. Dynamic Verbs: Resisting Present Continuous Overuse",
        "cefr_level": "B1",
        "category": "tenses_and_aspect",
        "summary_en": "Keep mental-state and possession verbs ('know', 'own', 'belong', 'prefer', 'seem') in Simple forms while spotting action exceptions like 'thinking' or 'having dinner'.",
        "summary_tr": "Türkçedeki '-yor' ekinin her fiile uyabilmesinin aksine, durum bildiren fiilleri ('know, belong, prefer, contain') İngilizcede sürekli kipte (-ing) kullanmayın.",
        "explanation_en": [
            {
                "title": "States vs. Voluntary Actions",
                "content": "Verbs expressing knowledge, belief, preference, ownership, or appearance take Simple Present for present truth ('I prefer option B'). A few dual verbs switch meaning in Continuous: 'have' (possession) vs. 'having lunch' (event), 'think' (opinion) vs. 'thinking about' (active pondering).",
                "patterns": [
                    "Stative: know / own / contain / seem / believe (NO -ing)",
                    "Dynamic shift: think (opinion) vs. be thinking about (considering)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Biliyorum, istiyorum, sahip oluyorum, içeriyor' tamamen doğaldır. Bu yüzden '*I am knowing', '*It is containing' gibi kalıplar yaygın B1 hatalarıdır.",
        "rules": [
            {
                "name": "Simple Forms for Mental & Ownership States",
                "pattern": "Subject + Base/Simple Verb (know/need/contain/belong)",
                "use_cases": ["Describing system specifications, user ownership, and preferences"],
                "time_markers": ["currently", "right now"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "I think this offline schema is clean.",
                "structure_b": "I am thinking about splitting the vocabulary script.",
                "difference_explanation_en": "'Think' (opinion) stays Simple; 'thinking about' signals active ongoing deliberation.",
                "difference_explanation_tr": "Görüş bildirirken 'think' sade kalır; zihinde tartma eyleminde 'thinking about' kullanılır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Türkçe '-yor' Etkisiyle Durum Fiillerine '-ing' Eklemek",
                "explanation_tr": "'Dosya 500 satır içeriyor' cümlesini '*The file is containing 500 rows' diye çevirmek '-yor' aktarımından doğar; doğrusu 'contains'tır.",
                "incorrect_example": "Right now I am needing your password and this module is belonging to billing.",
                "correct_example": "Right now I need your approval and this module belongs to billing.",
            }
        ],
        "examples": [
            {
                "en": "The local package currently contains over five thousand validated entries.",
                "tr": "Yerel paket şu anda beş binden fazla doğrulanmış kayıt içeriyor.",
                "rule_highlight": "currently contains",
                "context": "technical status",
            },
            {
                "en": "Leadership is weighing whether to expand speaking coaching next quarter.",
                "tr": "Yönetim gelecek çeyrekte konuşma koçluğunu genişletmeyi değerlendiriyor.",
                "rule_highlight": "is weighing whether",
                "context": "strategy",
            },
        ],
        "topic_tags": ["tenses", "aspect", "turkish_l1_traps"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b1.question-tags-and-echo-replies",
        "title": "Question Tags and Polite Confirmation Replies vs. Invariant 'Değil Mi?'",
        "cefr_level": "B1",
        "category": "modals_and_semi_modals",
        "summary_en": "Match question tag auxiliary verbs and polarity inversely to the main clause instead of appending an invariant 'isn't it?'.",
        "summary_tr": "Türkçedeki sabit 'değil mi?' yerine İngilizcede ana cümlenin yardımcı fiiline ve olumlu/olumsuz zıtlığına göre değişen soru kuyrukları (tag questions) kurun.",
        "explanation_en": [
            {
                "title": "Matching Polarity, Auxiliary, and Pronoun",
                "content": "Positive statements take negative tags; negative statements take positive tags. The tag always borrows the tense auxiliary ('do/does/did', 'have/has', 'will', 'can', 'is/are') and replaces the noun subject with a pronoun.",
                "patterns": [
                    "Positive Clause ('You synced the files') + Negative Tag (', didn't you?')",
                    "Negative Clause ('It hasn't crashed') + Positive Tag (', has it?')",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Geldin değil mi?', 'Seviyorsun değil mi?' kalıpları hep aynı sabit ifadeyi kullanır. İngilizcede '*You called him, isn't it?' yanlış aktarımdır; 'did' veya 'didn't' eşleşmelidir.",
        "rules": [
            {
                "name": "Polarity Flip + Tense Auxiliary",
                "pattern": "Main Clause (+ / -) + , + Auxiliary (- / +) + Subject Pronoun?",
                "use_cases": ["Checking shared understanding softly in meetings and live calls"],
                "time_markers": ["right now", "yesterday"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The validator passed all checks, didn't it?",
                "structure_b": "There wasn't a duplicate key left, was there?",
                "difference_explanation_en": "Past Simple uses 'didn't it'; 'there wasn't' flips polarity and keeps existential 'there'.",
                "difference_explanation_tr": "Geçmiş zamanlı cümlede 'didn't it', 'there' yapısında ise 'was there' kullanılır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "tense_transfer",
                "trap_title": "Her Cümlenin Sonuna Sabit 'Isn't It?' Eklemek",
                "explanation_tr": "'Değil mi?' karşılığı olarak özne ve zaman gözetmeden 'isn't it?' demek doğallığı bozar. Ana yüklemin yardımcı fiilini çekin.",
                "incorrect_example": "You already emailed the invoice to accounting, isn't it?",
                "correct_example": "You already emailed the invoice to accounting, didn't you?",
            }
        ],
        "examples": [
            {
                "en": "We agreed on freezing schema edits until Monday, didn't we?",
                "tr": "Pazartesiye kadar şema değişikliklerini dondurmakta anlaşmıştık, değil mi?",
                "rule_highlight": "didn't we",
                "context": "team sync",
            },
            {
                "en": "I am scheduled for the first client slot tomorrow, aren't I?",
                "tr": "Yarın ilk müşteri oturumu benim takvimimde, değil mi?",
                "rule_highlight": "aren't I",
                "context": "calendar check",
            },
        ],
        "topic_tags": ["speaking", "meetings", "modals"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    # ==================== B2 (10 lessons) ====================
    {
        "id": "grammar.b2.have-get-something-done-service",
        "title": "Delegated Service Causatives: Have / Get Something Done",
        "cefr_level": "B2",
        "category": "passive_and_causative",
        "summary_en": "Use 'have/get + Object + Past Participle (V3)' when commissioning an external service or suffering an external action rather than performing it yourself.",
        "summary_tr": "İşi bizzat yapmayıp başkasına veya bir uzmana yaptırdığınızı anlatırken etken fiil yerine 'have/get + Nesne + V3' ettirgen yapısını kullanın.",
        "explanation_en": [
            {
                "title": "Commissioning External Specialists vs. Personal Execution",
                "content": "Saying 'I translated the contract' implies you personally did the work. Saying 'We had the contract certified by outside counsel' clearly places the noun object directly between 'have/get' and the past participle.",
                "patterns": [
                    "have / get + Object + Past Participle (V3)",
                    "have someone do something (agent focus) vs. have something done (outcome focus)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Sözleşmeyi tercüme ettirdim' ile 'Sözleşmeyi tercüme ettim' arasındaki fark İngilizcede yalnızca nesneyi ortaya alıp V3 kullanarak kurulur. 'We updated the audit' kendi yaptığınız, 'We had the audit verified' uzmanına yaptırdığınız anlamındadır.",
        "rules": [
            {
                "name": "Service Causative Word Order",
                "pattern": "Subject + have/get (any tense) + Object + V3 (+ by Agent)",
                "use_cases": ["Reporting outsourced audits, repairs, medical checks, and legal reviews"],
                "time_markers": ["before signing", "last quarter"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "We audited the pricing ledger.",
                "structure_b": "We had the pricing ledger audited independently.",
                "difference_explanation_en": "Active voice claims internal execution; causative 'had... audited' specifies delegated third-party review.",
                "difference_explanation_tr": "Etken çatı işi içeride yaptığınızı, 'had... audited' ise bağımsız uzmana denetlettiğinizi belirtir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "voice_and_causative_transfer",
                "trap_title": "Nesne ile Geçmiş Ortaç (V3) Sırasını Karıştırmak veya Etken Bırakmak",
                "explanation_tr": "'Raporu imzalattım' anlamını '*I signed the report to someone' ya da '*I had certified the lease' şeklinde kurmak yanlıştır; nesne 'have/get' ile 'V3' arasına girer.",
                "incorrect_example": "We had notarized the power of attorney before the Ankara trip.",
                "correct_example": "We had the power of attorney notarized before the Ankara trip.",
            }
        ],
        "examples": [
            {
                "en": "Let us get the offline APK tested on three mid-range Android tablets first.",
                "tr": "Önce çevrimdışı APK'yı üç orta segment Android tablette test ettirelim.",
                "rule_highlight": "get the offline APK tested",
                "context": "release QA",
            },
            {
                "en": "She is having the compliance addendum translated prior to Friday's call.",
                "tr": "Cuma günkü görüşmeden önce uyumluluk ek protokolünü tercüme ettiriyor.",
                "rule_highlight": "is having the compliance addendum translated",
                "context": "legal coordination",
            },
        ],
        "topic_tags": ["causative", "legal", "operations"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.future-in-the-past-was-going-to",
        "title": "Future in the Past: Unfulfilled Plans and Past Projections",
        "cefr_level": "B2",
        "category": "tenses_and_aspect",
        "summary_en": "Shift future intentions back using 'was/were going to', 'would', and 'was supposed to' when explaining changed timelines or past expectations.",
        "summary_tr": "Geçmişte planlanmış ancak gerçekleşmemiş veya sonradan değişmiş hedefleri aktarırken 'was/were going to', 'would' ve 'was supposed to' yapılarını kullanın.",
        "explanation_en": [
            {
                "title": "Backshifting Future Commitments Accurately",
                "content": "Direct 'will' and 'is going to' describe today's outlook. Once reporting yesterday's canceled plan or an expectation held last week, shift to 'was going to' (prior intention) or 'was supposed to' (missed expectation/obligation).",
                "patterns": [
                    "was/were going to + Base Verb (+ but...)",
                    "was/were supposed to + Base Verb (expected obligation not kept)",
                ],
            }
        ],
        "explanation_tr": "Türkçede '-ecektik ama...' yapısı geçmişte planlanan ancak yapılmayan işleri anlatır. İngilizcede bunun birebir karşılığı '*we will ship but...' değil 'we were going to ship, but...' kalıbıdır.",
        "rules": [
            {
                "name": "Revised Plans ('Was Going To')",
                "pattern": "Subject + was/were going to + V1 (+ pivot clause)",
                "use_cases": ["Explaining delayed releases and shifting project priorities professionally"],
                "time_markers": ["originally", "last Monday", "until the blocker appeared"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "We were going to launch on Thursday.",
                "structure_b": "The partner was supposed to send credentials by noon.",
                "difference_explanation_en": "'Was going to' expresses internal intent; 'was supposed to' highlights an unfulfilled duty or schedule.",
                "difference_explanation_tr": "'Was going to' kendi niyetimizi, 'was supposed to' ise beklenen/gereken sorumluluğu anlatır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "tense_transfer",
                "trap_title": "Geçmişteki Gelecek Planlarında 'Will' Kullanmak",
                "explanation_tr": "'Geçen hafta yayınlayacaktık' anlamında '*I thought we will launch last week' demek zaman uyumunu bozar; ana fiil geçmişse 'would' veya 'were going to' seçilmelidir.",
                "incorrect_example": "Finance expected that cloud spending will drop last quarter.",
                "correct_example": "Finance expected that cloud spending would drop last quarter.",
            }
        ],
        "examples": [
            {
                "en": "We were going to archive older audio files, yet regional demand pushed expansion instead.",
                "tr": "Eski ses dosyalarını arşivleyecektik ancak bölgesel talep bunun yerine genişlemeyi tetikledi.",
                "rule_highlight": "were going to archive",
                "context": "project retrospective",
            },
            {
                "en": "None of us realized the customs clearance would take four extra business days.",
                "tr": "Hiçbirimiz gümrük işlemlerinin dört iş günü daha süreceğini fark etmemişti.",
                "rule_highlight": "would take",
                "context": "operations review",
            },
        ],
        "topic_tags": ["tenses", "planning", "reporting"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.if-only-wish-vs-hope",
        "title": "Realistic Hope vs. Counterfactual Wish & If Only",
        "cefr_level": "B2",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "Keep achievable future outcomes with 'hope + Present/Will', while reserving 'wish / if only + Past / Past Perfect / Would' for counterfactual regrets and complaints.",
        "summary_tr": "Gerçekleşebilir dileklerde 'hope' + düz zaman kullanırken, gerçeğe aykırı şimdiki durumlar veya geçmiş pişmanlıklar için 'wish / if only' + zaman kaydırması uygulayın.",
        "explanation_en": [
            {
                "title": "Achievable Future vs. Counterfactual Unreal Past",
                "content": "'Hope' handles genuine possibilities ('I hope telemetry holds steady'). 'Wish' signals an unreal/unlikely situation: Past Simple for present regret ('I wish we had more time'), Past Perfect for past mistakes ('If only we had checked sooner'), and 'wish + would' for wishing another actor would change behavior.",
                "patterns": [
                    "hope + Subject + Present / will / can",
                    "wish + Subject + Past Simple / had + V3 / would + V1",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Umarım gelir' ile 'Keşke burada olsaydı' yapıları günlük konuşmada 'wish' ile karıştırılıp '*I wish it passes tomorrow' gibi kurulabiliyor. Geleceğe dönük makul umutlarda yalnızca 'hope' kullanılır.",
        "rules": [
            {
                "name": "Present 'Hope' vs. Counterfactual 'Wish'",
                "pattern": "hope + Present/Future | wish + Past Simple (now) / had V3 (past)",
                "use_cases": ["Distinguishing optimistic sign-offs from polite diplomatic regret"],
                "time_markers": ["hopefully", "if only", "back in Q2"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "I hope the client approves the revised terms.",
                "structure_b": "I wish the client didn't insist on bespoke billing.",
                "difference_explanation_en": "'Hope' expresses a realistic future outcome; 'wish + didn't' laments a stubborn present fact.",
                "difference_explanation_tr": "'Hope' olası bir beklentiyi, 'wish + didn't' ise şu an değişmesi zor bir durumu üzüntüyle bildirir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "Olası Gelecek Dileklerinde 'Hope' Yerine '*I Wish + Will' Kurmak",
                "explanation_tr": "'İnşallah / Umarım yarın onaylanır' derken '*I wish it will be approved' yanlıştır. Gelecekteki umutlarda 'I hope it is approved / will be approved' kullanılmalıdır.",
                "incorrect_example": "We wish the regulatory board will publish clarity by Friday.",
                "correct_example": "We hope the regulatory board publishes clarity by Friday.",
            }
        ],
        "examples": [
            {
                "en": "I hope this clearer schedule settles the invoicing backlog this week.",
                "tr": "Umarım bu daha net takvim bu hafta faturalama birikmesini çözer.",
                "rule_highlight": "hope this clearer schedule settles",
                "context": "client update",
            },
            {
                "en": "I wish suppliers would stop sending unindexed PDF tables instead of CSV.",
                "tr": "Keşke tedarikçiler CSV yerine dizinsiz PDF tablolar göndermeyi bıraksa.",
                "rule_highlight": "wish suppliers would stop",
                "context": "operations complaint",
            },
        ],
        "topic_tags": ["conditionals", "diplomacy", "emails"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.had-better-vs-should-would-rather",
        "title": "Urgent 'Had Better' vs. General 'Should' vs. Preference 'Would Rather'",
        "cefr_level": "B2",
        "category": "modals_and_semi_modals",
        "summary_en": "Separate urgent consequence warnings ('had better'), neutral counsel ('should'), and two-subject polite preferences ('would rather someone did').",
        "summary_tr": "Somut risk uyarısı ('had better'), genel tavsiye ('should') ve başka birinden beklenti tercihi ('would rather + Past Simple') kalıplarını karıştırmayın.",
        "explanation_en": [
            {
                "title": "Why 'Had Better' Never Signals Polite Hospitality",
                "content": "Contrary to learner intuition, 'You had better check...' sounds urgent or slightly menacing—implying bad consequences if skipped—so never use it casually to offer coffee! Use 'would rather' for preferences; when preferring what someone else does, 'would rather' triggers Past Simple subjunctive.",
                "patterns": [
                    "had better (+ not) + V1 (NO 'to'—urgent risk advice)",
                    "would rather + Subject + Past Simple (present/future preference for another agent)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Yapsan iyi olur' kibar bir öneri de olabilir; ancak İngilizcede 'You'd better...' uyarı/tehdit tınısı taşır. Ayrıca 'tercih ederim ki o yapsın' anlamında 'would rather + he/they + V2' (geçmiş kip) kullanılır.",
        "rules": [
            {
                "name": "Same-Subject vs. Different-Subject 'Would Rather'",
                "pattern": "would rather + V1 (same subject) | would rather + Object/Subj + V2 (other agent)",
                "use_cases": ["Declining requests tactfully and flagging high-stakes operational risks"],
                "time_markers": ["before deployment", "this afternoon"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "You should review optional styling later.",
                "structure_b": "We had better verify backups before running the wipe script.",
                "difference_explanation_en": "'Should' offers balanced advice; 'had better' warns of real damage if ignored.",
                "difference_explanation_tr": "'Should' genel öneri verirken 'had better' yapılmazsa doğacak somut zarara karşı uyarır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "tense_transfer",
                "trap_title": "'Had Better' Sonrası 'To' Koymak ve Farklı Öznede Geniş Zaman Kullanmak",
                "explanation_tr": "'*You had better to test' ve '*I would rather you send it today' hatalıdır; ilki 'to' almaz, ikincisi ise başka özne olduğu için 'sent' ister.",
                "incorrect_example": "We had better to wait, and I would rather finance reviews the invoice now.",
                "correct_example": "We had better wait, and I would rather finance reviewed the invoice now.",
            }
        ],
        "examples": [
            {
                "en": "I would rather we resolved the schema mismatch offline before alerting beta testers.",
                "tr": "Beta testçilerine haber vermeden önce şema uyuşmazlığını çevrimdışında çözmemizi tercih ederim.",
                "rule_highlight": "would rather we resolved",
                "context": "release planning",
            },
            {
                "en": "We had better not push unvalidated CSV exports right before the holiday weekend.",
                "tr": "Tatil hafta sonundan hemen önce doğrulanmamış CSV dışa aktarımları göndermesek iyi olur.",
                "rule_highlight": "had better not push",
                "context": "operations warning",
            },
        ],
        "topic_tags": ["modals", "negotiation", "register"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.participle-vs-relative-modifiers",
        "title": "Active '-ing' vs. Passive '-ed' Noun Modifiers in Technical Writing",
        "cefr_level": "B2",
        "category": "relative_and_participle_clauses",
        "summary_en": "Choose present participles ('tickets blocking release') when the noun performs the action and past participles ('builds audited yesterday') when acted upon.",
        "summary_tr": "İsimden sonra gelen kısaltılmış sıfat cümleciklerinde nitelediği isim etken ise '-ing', edilgen ise '-ed/V3' seçerek teknik özetleri sıkılaştırın.",
        "explanation_en": [
            {
                "title": "Compressing 'Which/That' Relatives Without Voice Errors",
                "content": "Executive writing condenses 'bugs that affect offline startup' into 'bugs affecting offline startup', and 'entries that were curated locally' into 'entries curated locally'. Watch out for transitive vs. intransitive logic!",
                "patterns": [
                    "Noun + V-ing... (= which/that actively performs verb)",
                    "Noun + V3... (= which/that is acted on passively)",
                ],
            }
        ],
        "explanation_tr": "Türkçede '-en' (yapan) ile '-ilen' (yapılan) ekleri ortaça kaynaştığı için öğrenciler bazen edilgen isimde '*the files containing in the folder' gibi '-ing' hatası yapar; doğrusu 'contained' olmalıdır.",
        "rules": [
            {
                "name": "Voice Check on Post-Nominal Participles",
                "pattern": "Doer Noun + V-ing | Receiver Noun + V3",
                "use_cases": ["Condensing issue trackers, contracts, and executive bullet lists"],
                "time_markers": ["submitted yesterday", "pending sign-off"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The clause limiting vendor liability sits on page four.",
                "structure_b": "The clause limited by counsel yesterday sits on page four.",
                "difference_explanation_en": "'Limiting' = the clause enforces a limit; 'limited' = counsel constrained the clause.",
                "difference_explanation_tr": "'Limiting' maddenin kendisinin sınırladığını; 'limited' ise hukuk ekibi tarafından sınırlandığını anlatır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "voice_and_causative_transfer",
                "trap_title": "Edilgen İsim Nitelemesinde Yanlışlıkla '-ing' Kullanmak",
                "explanation_tr": "'Belgede yer alan veriler' veya 'denetimden geçen dosyalar' ifadelerinde pasif anlam olduğu halde '*data containing in the sheet' yazmak yaygındır.",
                "incorrect_example": "Please verify all rows generating by the afternoon script before merge.",
                "correct_example": "Please verify all rows generated by the afternoon script before merge.",
            }
        ],
        "examples": [
            {
                "en": "Every scenario packaged in the offline SQLite archive passes schema validation.",
                "tr": "Çevrimdışı SQLite arşivinde paketlenen her senaryo şema doğrulamasından geçer.",
                "rule_highlight": "packaged in the offline SQLite archive",
                "context": "build engineering",
            },
            {
                "en": "Users experiencing intermittent cellular drops still finish daily vocabulary reviews.",
                "tr": "Aralıklı hücresel kesinti yaşayan kullanıcılar günlük kelime tekrarlarını yine de tamamlar.",
                "rule_highlight": "experiencing intermittent cellular drops",
                "context": "product telemetry",
            },
        ],
        "topic_tags": ["participles", "engineering", "writing"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.gradable-vs-extreme-adjectives",
        "title": "Degree Modifiers with Gradable vs. Extreme Adjectives",
        "cefr_level": "B2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Pair ordinary gradable adjectives ('useful', 'expensive') with 'very / fairly / extremely', and non-gradable extreme adjectives ('vital', 'exhaustive', 'identical') with 'absolutely / utterly / virtually'.",
        "summary_tr": "Derecelendirilebilen sıfatları ('useful, slow') 'very / extremely' ile, uç/mutlak sıfatları ('essential, exhausted, impossible') ise 'absolutely / completely' zarflarıyla eşleştirin.",
        "explanation_en": [
            {
                "title": "Precision in Executive Emphasis",
                "content": "Calling a security fix 'very critical' or 'very unique' breaks natural collocation because extreme/absolute adjectives already sit at the limit. Reserve 'very/extremely' for scalar traits and 'absolutely/entirely' for absolute traits; 'really' works flexibly in both.",
                "patterns": [
                    "very / quite / slightly + Gradable Adjective ('very reliable')",
                    "absolutely / completely / virtually + Non-Gradable ('absolutely essential')",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'çok eşsiz', 'çok kritik' veya 'çok imkânsız' deyişleri kulağa doğal gelse de İngilizcede mutlak sıfatlarla '*very impossible / very exhausted' eşdizimlilik hatası verir.",
        "rules": [
            {
                "name": "Scalar vs. Limit Adverb Pairing",
                "pattern": "very + [good/slow/costly] | absolutely + [excellent/vital/identical]",
                "use_cases": ["Writing calibrated executive reviews without overstated filler"],
                "time_markers": ["consistently", "across regions"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Offline search is very responsive.",
                "structure_b": "Deterministic validation is absolutely vital.",
                "difference_explanation_en": "'Responsive' sits on a scale ('very'); 'vital' sits at an absolute ceiling ('absolutely').",
                "difference_explanation_tr": "'Responsive' dereceli sıfattır ('very'); 'vital' mutlak eşik sıfatıdır ('absolutely').",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "Mutlak veya Uç Sıfatların Önüne 'Very' Getirmek",
                "explanation_tr": "'Çok harika' veya 'çok kusursuz' çevirisiyle '*very excellent / *very impossible' yazmak C1/B2 değerlendirmelerinde puan düşüren eşdizim hatasıdır.",
                "incorrect_example": "Leaving plaintext keys in APK assets would be a very disastrous breach.",
                "correct_example": "Leaving plaintext keys in APK assets would be an utterly disastrous breach.",
            }
        ],
        "examples": [
            {
                "en": "The two database builds were virtually identical down to every SHA-256 checksum byte.",
                "tr": "İki veritabanı derlemesi her SHA-256 sağlama baytına kadar neredeyse birebir aynıydı.",
                "rule_highlight": "virtually identical",
                "context": "determinism verification",
            },
            {
                "en": "Negotiators found legal pricing terms fairly balanced despite earlier skepticism.",
                "tr": "Müzakereciler önceki şüphelere rağmen hukuki fiyat şartlarını oldukça dengeli buldu.",
                "rule_highlight": "fairly balanced",
                "context": "contract review",
            },
        ],
        "topic_tags": ["adjectives", "writing", "register"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.preposition-stranding-vs-formal-fronting",
        "title": "Preposition Stranding in Speech vs. Preposition Fronting in Formal Prose",
        "cefr_level": "B2",
        "category": "prepositions_and_particles",
        "summary_en": "Keep prepositions naturally at the end of spoken questions/clauses ('the client we met with'), while fronting them with 'whom/which' in formal legal and executive writing ('the terms to which we agreed').",
        "summary_tr": "Günlük konuşmada edatları cümle sonunda bırakırken ('whom/that' atıp 'we spoke with' diyerek), resmi yazıda edatı 'which/whom' önüne alarak düzeyi yükseltin.",
        "explanation_en": [
            {
                "title": "Register Control Without Duplicating Prepositions",
                "content": "Both 'the firm that we work with' (everyday) and 'the firm with which we work' (formal) are correct—provided the preposition appears ONCE, and never after 'that' ('*with that we work' is forbidden).",
                "patterns": [
                    "Conversational: Noun + (that/who) + Subject + Verb + Preposition",
                    "Formal: Noun + Preposition + which / whom + Subject + Verb",
                ],
            }
        ],
        "explanation_tr": "Türkçe ilgi tümceciklerinde edat önde gelir veya anlam fiile gömülür; bu yüzden İngilizcede bazen edat iki kez konur ('*with which we met with') veya 'that' önüne taşınır ('*with that'). Edat tek yerde kalmalıdır.",
        "rules": [
            {
                "name": "No Preposition Before 'That' or 'Who'",
                "pattern": "Preposition + which / whom (NEVER Preposition + that / who)",
                "use_cases": ["Adapting between spoken meeting updates and signed contract clauses"],
                "time_markers": ["currently", "under this agreement"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The vendor we contracted with passed inspection.",
                "structure_b": "The vendor with whom we contracted passed inspection.",
                "difference_explanation_en": "Identical logic: stranding sounds natural orally; fronting before 'whom' raises formal authority.",
                "difference_explanation_tr": "Aynı anlam: sonda kalan 'with' konuşmada doğal, 'with whom' ise resmi metinde saygın durur.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "Edatı 'That' Önüne Almak veya Cümle İçinde Tekrarlamak",
                "explanation_tr": "'Bahsettiğimiz konu' için '*the issue about that we spoke' ya da '*the issue about which we talked about' kurmak en yaygın edat kaydırma hatalarıdır.",
                "incorrect_example": "This is the audit threshold for which compliance depends on.",
                "correct_example": "This is the audit threshold on which compliance depends.",
            }
        ],
        "examples": [
            {
                "en": "The methodology upon which this deterministic validator rests is simple and transparent.",
                "tr": "Bu deterministik doğrulayıcının dayandığı metodoloji sade ve şeffaftır.",
                "rule_highlight": "upon which this deterministic validator rests",
                "context": "architecture spec",
            },
            {
                "en": "Who is the regional specialist I should check in with before filing expenses?",
                "tr": "Harcamaları girmeden önce teyit almam gereken bölge uzmanı kim?",
                "rule_highlight": "check in with",
                "context": "workplace chat",
            },
        ],
        "topic_tags": ["prepositions", "register", "legal"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.passive-reporting-need-doing-suppose",
        "title": "Passive Maintenance and Scheduled Obligations: Need Doing / Be Supposed To",
        "cefr_level": "B2",
        "category": "passive_and_causative",
        "summary_en": "Express maintenance needs cleanly using 'needs + V-ing' (or 'needs to be + V3') and distinguish intended rules ('be supposed to') from binding mandates ('be to').",
        "summary_tr": "Bakım ve düzeltme ihtiyaçlarında aktif görünüşlü pasif anlam taşıyan 'needs fixing / updating' yapısını ve planlanan beklentilerde 'be supposed to' kalıbını doğru kullanın.",
        "explanation_en": [
            {
                "title": "Active Form, Passive Meaning",
                "content": "When a system or file requires repair without naming who will repair it, English allows 'The test suite needs updating' alongside 'The test suite needs to be updated'. Meanwhile, 'is supposed to' contrasts expected policy against messy reality.",
                "patterns": [
                    "Inanimate Subject + needs / requires + V-ing (= needs to be V3)",
                    "Subject + is/was supposed to + V1 (expected duty, often bent or missed)",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Dosyanın güncellenmesi gerekiyor' edilgen görünür; öğrenciler bunu görünce '*The file needs to update' (sanki dosya kendi kendine güncelleyecekmiş gibi) yazar. Ya 'needs updating' ya da 'needs to be updated' kullanın.",
        "rules": [
            {
                "name": "Repair & Maintenance Constructions",
                "pattern": "Object + needs + V-ing == Object + needs to be + V3",
                "use_cases": ["Writing QA bug triage notes and facility maintenance tickets"],
                "time_markers": ["before merge", "weekly"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "The onboarding flow needs revising.",
                "structure_b": "We were supposed to freeze schema edits at noon.",
                "difference_explanation_en": "'Needs revising' equals 'needs to be revised'; 'was supposed to' flags an expected schedule checkpoint.",
                "difference_explanation_tr": "'Needs revising' edilgen bakım gereksinimini; 'was supposed to' ise uyulması beklenen kuralı anlatır.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "voice_and_causative_transfer",
                "trap_title": "Cansız Nesneyle 'Needs To Update' Kurmak",
                "explanation_tr": "Cansız bir nesnenin ('test', 'belge', 'yazıcı') kendi kendine eylem yapacağı sanısını veren '*needs to fix' hatasına düşmeyin; pasif veya '-ing' seçin.",
                "incorrect_example": "That hardcoded row count in the test suite needs to update.",
                "correct_example": "That hardcoded row count in the test suite needs updating.",
            }
        ],
        "examples": [
            {
                "en": "Three outdated localization strings in settings still need reviewing.",
                "tr": "Ayarlardaki üç eski yerelleştirme metninin hâlâ gözden geçirilmesi gerekiyor.",
                "rule_highlight": "still need reviewing",
                "context": "bug triage",
            },
            {
                "en": "Temporary sandbox keys are supposed to expire automatically after each test run.",
                "tr": "Geçici deneme anahtarlarının her test çalıştırmasından sonra otomatik sona ermesi gerekir.",
                "rule_highlight": "are supposed to expire",
                "context": "security policy",
            },
        ],
        "topic_tags": ["passive", "engineering", "operations"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.contrast-on-contrary-vs-other-hand",
        "title": "Sharpening Contrast Linkers: On the Other Hand vs. On the Contrary vs. In Contrast",
        "cefr_level": "B2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Use 'on the other hand' for balancing two true sides, 'on the contrary' solely to reject a false claim outright, and 'in contrast' to compare two entities.",
        "summary_tr": "'On the other hand' iki geçerli yönü dengelerken, 'on the contrary' önceki yanlış yargıyı tamamen çürütmek için kullanılır; 'in contrast' iki özneyi karşılaştırır.",
        "explanation_en": [
            {
                "title": "Do Not Confuse Balancing with Rebuttal",
                "content": "Calling 'on the contrary' a synonym for 'on the other hand' reverses your intended logic. If you say 'Cloud APIs scale easily. On the contrary, they cost money,' you sound as if scaling isn't true! Use 'On the other hand' (or 'However') when adding a trade-off to a still-valid point.",
                "patterns": [
                    "Claim A (valid). On the other hand / Conversely, Trade-off B (also valid).",
                    "Negative/Misguided Claim. On the contrary, Opposite Truth!",
                ],
            }
        ],
        "explanation_tr": "Türkçedeki 'aksine' ile 'öte yandan' İngilizcede sıklıkla 'on the contrary' altında birleştirilir. Oysa 'on the contrary' bir iddiayı yalanlayıp tam tersini savunur; dengeleyici karşılaştırma için 'on the other hand' seçilir.",
        "rules": [
            {
                "name": "Rebuttal vs. Trade-Off Balancing",
                "pattern": "Denied assumption + On the contrary + Correction | Pros + On the other hand + Cons",
                "use_cases": ["Structuring persuasive strategy memos and analytical essays accurately"],
                "time_markers": ["however", "in contrast"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Local SQLite stays fast; on the other hand, initial packaging takes discipline.",
                "structure_b": "Offline packaging did not slow builds; on the contrary, tests ran faster.",
                "difference_explanation_en": "Example A weighs pros vs. cons; Example B denies 'slowed builds' completely.",
                "difference_explanation_tr": "A örneği artı-eksiyi tartar; B örneği önceki olumsuz iddiayı tamamen yalanlar.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "conditional_overgeneralization",
                "trap_title": "İki Doğru Seçeneği Karşılaştırırken 'On the Contrary' Kullanmak",
                "explanation_tr": "'Çevrimdışı mod güvenli; aksine güncelleme paket gerektirir' derken 'on the contrary' seçmek önceki cümleyi inkâr etmiş gibi mantık hatası oluşturur.",
                "incorrect_example": "Voice coaching boosts engagement; on the contrary, reading mode stays quiet.",
                "correct_example": "Voice coaching is dynamic; in contrast, reading mode stays intentionally quiet.",
            }
        ],
        "examples": [
            {
                "en": "Local caching removes network lag; on the other hand, bundle validation needs strict discipline.",
                "tr": "Yerel önbellekleme ağ gecikmesini ortadan kaldırır; öte yandan paket doğrulaması sıkı disiplin ister.",
                "rule_highlight": "on the other hand",
                "context": "architecture review",
            },
            {
                "en": "Adding Turkish trap notes did not slow learners down; on the contrary, mastery rose faster.",
                "tr": "Türkçe tuzak notları eklemek öğrencileri yavaşlatmadı; tam aksine ustalık daha hızlı yükseldi.",
                "rule_highlight": "on the contrary",
                "context": "research readout",
            },
        ],
        "topic_tags": ["discourse_markers", "writing", "logic"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
    {
        "id": "grammar.b2.negative-scope-hardly-none-not-all",
        "title": "Negative Scope Precision: None vs. Not All, Hardly, and Barely",
        "cefr_level": "B2",
        "category": "inversion_and_emphasis",
        "summary_en": "Prevent logical distortions by distinguishing partial negation ('not all entries need') from total negation ('no entries need'), and pairing 'hardly/scarcely' with positive verbs.",
        "summary_tr": "'Hepsi ... değil' (kısmi olumsuzluk) ile 'hiçbiri ...' (tam olumsuzluk) arasındaki kapsam farkını ayırın ve 'hardly/barely' zarflarıyla çift olumsuzluk kurmayın.",
        "explanation_en": [
            {
                "title": "Why 'All ... Are Not' Creates Ambiguity",
                "content": "Because English 'All employees didn't sign' can sound ambiguous, place 'not' clearly upfront ('Not all employees signed' = partial) vs. 'None of the employees signed' (= zero). Equally, 'hardly' and 'barely' already contain negation, so pair them with positive verbs: 'We could hardly hear', never '*couldn't hardly hear'.",
                "patterns": [
                    "Partial: Not all / Not every + Noun + Verb",
                    "Total: None of + the/Plural Noun + Verb (positive)",
                    "Positive Auxiliary + hardly / barely / scarcely",
                ],
            }
        ],
        "explanation_tr": "Türkçede 'Hepsi geçmedi' çoğunlukla 'bazıları kaldı' demekken İngilizce birebir çevirisi '*All didn't pass' kafa karıştırır. Kısmi durum için 'Not all...' veya 'Some didn't...', sıfır için 'None of them...' deyin.",
        "rules": [
            {
                "name": "Broad Negatives Take Positive Verbs",
                "pattern": "Subject + modal/verb (positive) + hardly / barely / seldom + Complement",
                "use_cases": ["Writing unambiguous legal disclaimers and executive audit summaries"],
                "time_markers": ["scarcely", "seldom", "barely"],
            }
        ],
        "contrasts": [
            {
                "structure_a": "Not all new entries require audio clips.",
                "structure_b": "None of the temp scripts remain after cleanup.",
                "difference_explanation_en": "'Not all' leaves a subset needing audio; 'None of' reduces the count strictly to zero.",
                "difference_explanation_tr": "'Not all' bazılarını kapsam dışı bırakır; 'None of' ise sayıyı kesin olarak sıfıra indirir.",
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "'Hardly / Barely' İle Olumsuz Yardımcı Fiili Birleştirmek",
                "explanation_tr": "'Neredeyse hiç anlamadım' anlamında '*I didn't barely understand' yazmak çift olumsuzluk hatasıdır; 'I barely understood' tek olumsuzluğu sağlar.",
                "incorrect_example": "Because of line noise, we couldn't hardly hear the client's final figure.",
                "correct_example": "Because of line noise, we could hardly hear the client's final figure.",
            }
        ],
        "examples": [
            {
                "en": "Not all regional branches run the same payroll cutoff schedule.",
                "tr": "Tüm bölge şubeleri aynı bordro kesim takvimini uygulamaz.",
                "rule_highlight": "Not all regional branches",
                "context": "policy writing",
            },
            {
                "en": "There was scarcely any latency difference between local and cached queries.",
                "tr": "Yerel ile önbellekli sorgular arasında neredeyse hiç gecikme farkı yoktu.",
                "rule_highlight": "was scarcely any latency difference",
                "context": "benchmark report",
            },
        ],
        "topic_tags": ["negation", "logic", "legal"],
        "related_ids": [],
        "status": "APPROVED",
        "version": 1,
    },
]

