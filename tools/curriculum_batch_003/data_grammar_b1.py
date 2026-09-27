#!/usr/bin/env python3
"""
Grammar Batch 003: B1 Lessons (10 lessons).
"""

from typing import List, Dict, Any

B1_LESSONS: List[Dict[str, Any]] = [
    {
        "id": "grammar.b1.so-do-i-neither-do-i-short-agreements",
        "title": "Short Additions and Agreements: 'So do I', 'Neither do I', and 'Nor have I'",
        "cefr_level": "B1",
        "category": "inversion_and_emphasis",
        "summary_en": "To agree with positive statements use 'So + auxiliary + subject'; to agree with negative statements use 'Neither / Nor + auxiliary + subject'.",
        "summary_tr": "Olumlu ifadelere katılmak için 'So + yardımcı fiil + özne'; olumsuz ifadelere katılmak için 'Neither / Nor + yardımcı fiil + özne' kalıbı kullanılır.",
        "explanation_en": [
            {
                "title": "Mirroring the Auxiliary Verb with Inversion",
                "content": "Short agreement formulas avoid repeating the full sentence. The auxiliary must match the tense and mood of the original statement: Present Simple uses do/does ('I love coffee' -> 'So do I'); Past Simple uses did ('I arrived early' -> 'So did we'); modals mirror themselves ('I can't attend' -> 'Neither can I'). Because of the inverted word order, the auxiliary immediately follows 'So' or 'Neither'.",
                "patterns": [
                    "Positive agreement: So + Auxiliary / Be + Subject (e.g., So am I / So do they)",
                    "Negative agreement: Neither / Nor + Auxiliary / Be + Subject (e.g., Neither have we / Nor can she)"
                ]
            }
        ],
        "explanation_tr": "Türkçede hem olumlu hem olumsuz cümlelere sadece 'Ben de' veya 'Biz de' denilerek cevap verilebilir. İngilizcede ise karşı taraf olumsuz bir cümle kurduysa ('I don't like meetings') 'So do I' denemez; mutlaka 'Neither do I' (Ben de sevmem) denmelidir. Ayrıca yardımcı fiil doğru zamandan seçilmelidir.",
        "rules": [
            {
                "name": "Polarity Agreement Inversion",
                "pattern": "Positive: So + Aux + Subj | Negative: Neither/Nor + Aux + Subj",
                "use_cases": [
                    "Expressing shared consensus during team discussions and negotiations",
                    "Agreeing casually with personal preferences, habits, and past experiences"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Me Too on Negative Statements Trap",
                "description_tr": "Karşı taraf olumsuz konuştuğunda 'Me too' demek İngilizcede kafa karıştırıcı veya hatalıdır; 'Me neither' veya 'Neither do I' kullanılmalıdır.",
                "trap_example": "A: 'I haven't finished the slides yet.' B: 'So do I.'",
                "correction": "Neither have I. / Nor have I.",
                "key_difference_tr": "Cümle olumsuz ('haven't') olduğu için 'So' yerine 'Neither', yardımcı fiil olarak da 'have' gelmelidir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "So do I (Positive)",
                "concept_b": "Neither do I (Negative)",
                "difference_en": "'So' confirms a shared affirmative state; 'Neither' confirms a shared negative state.",
                "difference_tr": "'So' olumlu onaylama, 'Neither' olumsuz onaylama yapar.",
                "example_a": "I work remotely on Mondays. — So do I.",
                "example_b": "I don't commute on Mondays. — Neither do I."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "I didn't like the presentation. — Neither I did.",
                "correct": "I didn't like the presentation. — Neither did I.",
                "explanation_en": "'Neither' triggers subject-auxiliary inversion, placing the auxiliary before the subject.",
                "explanation_tr": "'Neither' sonrasında yardımcı fiil özneden önce gelmek zorundadır ('Neither did I')."
            }
        ],
        "examples": [
            {
                "en": "We are planning to upgrade our cloud infrastructure soon. — So are we.",
                "tr": "Yakında bulut altyapımızı yükseltmeyi planlıyoruz. — Biz de öyle.",
                "context": "Inter-team technology planning discussion",
                "register": "neutral_workplace",
                "highlighted_phrase": "So are we"
            },
            {
                "en": "I haven't received the updated contract from the vendor. — Neither have I.",
                "tr": "Tedarikçiden güncellenmiş sözleşmeyi henüz almadım. — Ben de almadım.",
                "context": "Procurement team desk discussion",
                "register": "neutral_workplace",
                "highlighted_phrase": "Neither have I"
            },
            {
                "en": "She can speak both German and English fluently. — So can her colleague.",
                "tr": "Hem Almancayı hem de İngilizceyi akıcı bir şekilde konuşabiliyor. — İş arkadaşı da öyle.",
                "context": "Discussing international team skills",
                "register": "neutral_workplace",
                "highlighted_phrase": "So can"
            },
            {
                "en": "Our department didn't exceed its quarterly travel budget. — Nor did ours.",
                "tr": "Departmanımız üç aylık seyahat bütçesini aşmadı. — Bizimki de aşmadı.",
                "context": "Finance sync across business units",
                "register": "formal_written",
                "highlighted_phrase": "Nor did ours"
            }
        ],
        "topic_tags": ["communication", "work-career"]
    },
    {
        "id": "grammar.b1.question-tags-real-vs-checking",
        "title": "Question Tags: Verification vs. Genuine Inquiries",
        "cefr_level": "B1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Question tags turn statements into questions using inverted mini-clauses at the end; falling intonation seeks agreement, while rising intonation asks for genuine verification.",
        "summary_tr": "Soru ekleri (Question Tags), cümlenin sonuna eklenen ters kutuplu yardımcı fiillerle ifadeyi soruya dönüştürür; onay alma veya gerçek bilgi sorma amaçlı kullanılır.",
        "explanation_en": [
            {
                "title": "Polarity Inversion and Auxiliary Rules",
                "content": "A positive statement takes a negative tag ('You are ready, aren't you?'), while a negative statement takes a positive tag ('She hasn't signed, has she?'). Special forms include 'I am' -> 'aren't I?', 'Let's' -> 'shall we?', and imperatives -> 'will you / would you?'.",
                "patterns": [
                    "Positive statement + negative tag: Subject + Verb, Auxiliary + n't + Pronoun?",
                    "Negative statement + positive tag: Subject + Aux + not + Verb, Auxiliary + Pronoun?",
                    "Special exception: I am late, aren't I? / Let's begin, shall we?"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'değil mi?' sorusunun karşılığıdır. Türkçede her cümle sonuna tek bir 'değil mi?' gelirken, İngilizcede cümlenin ana fiiline ve zamanına uygun yardımcı fiil ters kutupta seçilmek zorundadır ('He is...' -> 'isn't he?', 'They went...' -> 'didn't they?').",
        "rules": [
            {
                "name": "Reverse Polarity Tag Rule",
                "pattern": "Positive statement -> Negative tag / Negative statement -> Positive tag",
                "use_cases": [
                    "Confirming project deadlines, consensus, or calendar details in meetings",
                    "Softening statements and inviting interlocutor feedback"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Universal Isn't It Trap",
                "description_tr": "Türkçedeki tek tip 'değil mi?' alışkanlığı nedeniyle her zaman 'isn't it?' demek ciddi bir kural hatasıdır.",
                "trap_example": "You received the tracking code this morning, isn't it?",
                "correction": "You received the tracking code this morning, didn't you?",
                "key_difference_tr": "Cümle geçmiş zaman ('received') olduğu için soru eki 'didn't you?' olmalıdır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Checking tag (Falling tone)",
                "concept_b": "Inquiry tag (Rising tone)",
                "difference_en": "Falling tone expects the listener to agree; rising tone indicates genuine uncertainty.",
                "difference_tr": "Düşen ton onay beklerken, yükselen ton gerçek bir merak ve belirsizlik belirtir.",
                "example_a": "It's a beautiful venue, isn't it? (Speaker is confident)",
                "example_b": "The train leaves at four, doesn't it? (Speaker is unsure)"
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "I am responsible for this project, amn't I?",
                "correct": "I am responsible for this project, aren't I?",
                "explanation_en": "The negative tag for 'I am' is irregularly 'aren't I?'.",
                "explanation_tr": "'I am' ifadesinin soru eki 'aren't I?' şeklindedir; 'amn't I' standart İngilizcede yoktur."
            }
        ],
        "examples": [
            {
                "en": "The client approved the revised scope document yesterday, didn't they?",
                "tr": "Müşteri revize edilen kapsam belgesini dün onayladı, değil mi?",
                "context": "Project manager verifying milestone with team",
                "register": "neutral_workplace",
                "highlighted_phrase": "didn't they"
            },
            {
                "en": "You don't need any additional software licenses for this sprint, do you?",
                "tr": "Bu sprint için ek bir yazılım lisansına ihtiyacınız yok, değil mi?",
                "context": "Scrum master checking tooling requirements",
                "register": "neutral_workplace",
                "highlighted_phrase": "do you"
            },
            {
                "en": "Let's review the marketing metrics together, shall we?",
                "tr": "Pazarlama metriklerini birlikte inceleyelim, ne dersiniz?",
                "context": "Team lead proposing joint task",
                "register": "formal_written",
                "highlighted_phrase": "shall we"
            },
            {
                "en": "They haven't announced the venue for the annual retreat yet, have they?",
                "tr": "Yıllık şirket inzivası için mekanı henüz duyurmadılar, değil mi?",
                "context": "Coworkers chatting during lunch",
                "register": "informal_spoken",
                "highlighted_phrase": "have they"
            }
        ],
        "topic_tags": ["work-career", "communication"]
    },
    {
        "id": "grammar.b1.verbs-with-prepositions-dependent",
        "title": "Dependent Prepositions: High-Frequency Verb-Preposition Collocations",
        "cefr_level": "B1",
        "category": "prepositions_and_particles",
        "summary_en": "Many English verbs require specific fixed prepositions (rely on, depend on, suffer from, complain about) regardless of the meaning in a learner's native language.",
        "summary_tr": "Birçok İngilizce fiil, Türkçedeki yönelme veya ayrılma eklerinden bağımsız olarak kendilerine özgü sabit edatlarla (rely on, complain about vb.) kullanılır.",
        "explanation_en": [
            {
                "title": "Arbitrary Collocational Prepositions",
                "content": "Dependent prepositions are fixed collocations that cannot be translated word-for-word. Common workplace pairs include: rely ON, depend ON, concentrate ON, apologize FOR, apply FOR, complain ABOUT, suffer FROM, prevent someone FROM, belong TO, and listen TO. If followed by a verb, that verb must take the gerund (-ing) form.",
                "patterns": [
                    "Verb + Dependent Preposition + Noun (e.g., rely on data)",
                    "Verb + Dependent Preposition + Gerund (e.g., apologize for arriving late)"
                ]
            }
        ],
        "explanation_tr": "Türkçe düşünürken 'ona güveniyorum' (yönelme -e) deriz, ancak İngilizce 'rely to' değil 'rely ON' gerektirir. 'Bundan şikayet etti' (ayrılma -den) ifadesi 'complain from' değil 'complain ABOUT' olur. Edatların sabit kalıplar olarak ezberlenmesi gerekir.",
        "rules": [
            {
                "name": "Prepositional Gerund Rule",
                "pattern": "Verb + Preposition + Verb-ing",
                "use_cases": [
                    "Formulating professional apologies, applications, and feedback",
                    "Analyzing dependencies and operational reliability in projects"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Complain From / Suffer Of Transfer Trap",
                "description_tr": "Türkçedeki 'şikayetçi olmak' ve 'muzdarip olmak' için -den eki kullanıldığı için 'from' veya 'of' edatını yanlış fiillere bağlamak yaygındır.",
                "trap_example": "Several users complained from the slow loading speed of the dashboard.",
                "correction": "Several users complained ABOUT the slow loading speed of the dashboard.",
                "key_difference_tr": "'Complain' fiili konuyu belirtirken 'about' alır; 'from' ile kullanılmaz."
            }
        ],
        "contrasts": [
            {
                "concept_a": "apply for (a position / permit)",
                "concept_b": "apply to (an institution / recipient)",
                "difference_en": "'Apply for' targets the desired object or role; 'apply to' targets the organization receiving the application.",
                "difference_tr": "'Apply for' başvurulan pozisyon veya şey; 'apply to' başvurulan merci için kullanılır.",
                "example_a": "She decided to apply for the senior engineer role.",
                "example_b": "He applied to three international universities."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "Our success depends of customer satisfaction.",
                "correct": "Our success depends ON customer satisfaction.",
                "explanation_en": "'Depend' collocated with 'on' or 'upon', never with 'of'.",
                "explanation_tr": "'Depend' fiili asla 'of' almaz, her zaman 'on' ile kullanılır."
            }
        ],
        "examples": [
            {
                "en": "Our deployment pipeline relies on automated end-to-end regression tests.",
                "tr": "Dağıtım hattımız otomatik uçtan uca regresyon testlerine güvenmektedir.",
                "context": "DevOps presentation to engineering team",
                "register": "neutral_workplace",
                "highlighted_phrase": "relies on"
            },
            {
                "en": "We must apologize for delaying the delivery of the hardware units.",
                "tr": "Donanım birimlerinin teslimatını geciktirdiğimiz için özür dileriz.",
                "context": "Customer relations notice",
                "register": "formal_written",
                "highlighted_phrase": "apologize for delaying"
            },
            {
                "en": "The product owner urged the team to concentrate on core user journeys.",
                "tr": "Ürün yöneticisi ekibe ana kullanıcı akışlarına odaklanmaları çağrısında bulundu.",
                "context": "Sprint goal definition meeting",
                "register": "neutral_workplace",
                "highlighted_phrase": "concentrate on"
            },
            {
                "en": "Regular physical breaks prevent engineers from experiencing repetitive strain injuries.",
                "tr": "Düzenli fiziksel molalar mühendislerin tekrarlayan zorlanma yaralanmaları yaşamasını önler.",
                "context": "Workplace ergonomic guidelines",
                "register": "neutral_workplace",
                "highlighted_phrase": "prevent engineers from"
            }
        ],
        "topic_tags": ["work-career", "communication"]
    },
    {
        "id": "grammar.b1.verbs-with-infinitive-vs-gerund-common",
        "title": "Verb Complementation: Common Verbs Followed by Infinitive vs. Gerund",
        "cefr_level": "B1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Certain verbs always take a to-infinitive (decide, hope, manage, promise), while others require a gerund (avoid, consider, suggest, postpone, risk).",
        "summary_tr": "Bazı fiiller kendisinden sonra daima 'to + mastar' alırken (decide, manage vb.), bazıları ise fiilimsi '-ing' (gerund) gerektirir (avoid, suggest, postpone vb.).",
        "explanation_en": [
            {
                "title": "Classifying Verb Patterns",
                "content": "English verbs govern whether a following verb appears as an infinitive or gerund. Verbs looking forward toward an intention often take infinitives: 'We decided to expand'. Verbs reflecting on an activity, avoiding it, or suggesting an ongoing action take gerunds: 'They avoided mentioning the cost', 'I suggest rescheduling the meeting'. Memorizing these distinct complementation patterns is critical for writing accuracy.",
                "patterns": [
                    "Verb + to-infinitive: plan to do, agree to do, manage to do, refuse to do, promise to do",
                    "Verb + gerund (-ing): avoid doing, suggest doing, consider doing, postpone doing, risk doing"
                ]
            }
        ],
        "explanation_tr": "Türkçede hepsi mastar eki (-mek/-mak) veya isim-fiil (-me/-ma) olarak çevrildiği için Türk öğrencilerin 'suggest to go' veya 'decide going' gibi hatalar yapması çok yaygındır. 'Suggest' kesinlikle 'to' almaz; gerund alır ('suggest going').",
        "rules": [
            {
                "name": "Complementation Class Rule",
                "pattern": "Group A (hope, decide, promise) + to V vs. Group B (suggest, avoid, delay) + V-ing",
                "use_cases": [
                    "Drafting action items, decisions, and meeting summaries",
                    "Communicating project intentions, proposals, and risk mitigation"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Suggest To Do Trap",
                "description_tr": "'Suggest' fiilinin ardından 'to do' kullanmak en yaygın L1 transfer hatasıdır; doğrudan fiil gelecekse -ing almalıdır.",
                "trap_example": "The consultant suggested to migrate our database to PostgreSQL.",
                "correction": "The consultant suggested migrating our database to PostgreSQL.",
                "key_difference_tr": "'Suggest' doğrudan fiil alacaksa daima gerund (-ing) gerektirir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "decide + to-infinitive",
                "concept_b": "consider + gerund",
                "difference_en": "'Decide' resolves on an action (to do); 'consider' reflects on a possibility (doing).",
                "difference_tr": "'Decide' kesin kararı (to do), 'consider' ise değerlendirilen olasılığı (doing) belirtir.",
                "example_a": "We decided to launch the feature next week.",
                "example_b": "We are considering launching the feature next week."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "We cannot risk to lose our primary enterprise client.",
                "correct": "We cannot risk losing our primary enterprise client.",
                "explanation_en": "'Risk' takes a gerund complement, not a to-infinitive.",
                "explanation_tr": "'Risk' fiilinden sonra gelen eylem '-ing' takısı alır."
            }
        ],
        "examples": [
            {
                "en": "The engineering team managed to resolve the outage within twenty minutes.",
                "tr": "Mühendislik ekibi sistem kesintisini yirmi dakika içinde çözmeyi başardı.",
                "context": "Post-incident engineering summary",
                "register": "neutral_workplace",
                "highlighted_phrase": "managed to resolve"
            },
            {
                "en": "We should avoid deploying major updates late on a Friday afternoon.",
                "tr": "Cuma günü öğleden sonraları büyük güncellemeler yayınlamaktan kaçınmalıyız.",
                "context": "Team operational best practices discussion",
                "register": "neutral_workplace",
                "highlighted_phrase": "avoid deploying"
            },
            {
                "en": "Management promised to provide additional testing equipment next month.",
                "tr": "Yönetim, önümüzdeki ay ek test ekipmanı sağlama sözü verdi.",
                "context": "Team meeting notes",
                "register": "neutral_workplace",
                "highlighted_phrase": "promised to provide"
            },
            {
                "en": "I suggest reviewing the user feedback before finalizing the UI layout.",
                "tr": "Kullanıcı arayüzü düzenini kesinleştirmeden önce kullanıcı geri bildirimlerini incelemeyi öneriyorum.",
                "context": "Design review workshop",
                "register": "neutral_workplace",
                "highlighted_phrase": "suggest reviewing"
            }
        ],
        "topic_tags": ["work-career", "communication"]
    },
    {
        "id": "grammar.b1.modals-of-ability-past-could-was-able-to",
        "title": "Past Ability: General Capacity ('Could') vs. Specific Achievement ('Was able to / Managed to')",
        "cefr_level": "B1",
        "category": "modals_and_semi_modals",
        "summary_en": "'Could' describes general past ability, while 'was / were able to' or 'managed to' must be used for a specific successful achievement on a single occasion.",
        "summary_tr": "'Could' geçmişteki genel yetenekleri anlatır; tek seferlik belirli bir zorluğun üstesinden gelinip başarılması durumunda ise 'was/were able to' veya 'managed to' kullanılır.",
        "explanation_en": [
            {
                "title": "General Ability vs. Specific One-Off Success",
                "content": "To express that someone possessed a skill over an extended period in the past, use 'could': 'He could code in C++ at age twelve'. However, in affirmative sentences describing a single specific achievement against obstacles, 'could' is ungrammatical; English requires 'was/were able to' or 'managed to': 'The server crashed, but we were able to restore the database'. In the negative, 'couldn't' can be used for both general and specific contexts.",
                "patterns": [
                    "General past ability: Subject + could + Base Verb (e.g., I could swim well as a child)",
                    "Specific past achievement: Subject + was/were able to + Base Verb (e.g., We were able to sign the client)",
                    "Overcoming obstacle: Subject + managed to + Base Verb (e.g., She managed to catch the train)"
                ]
            }
        ],
        "explanation_tr": "Türkçede hem 'yapabildim' (genel yetenek) hem de 'yapabildim' (o gün o işi hallettim) aynı şekilde söylenir. İngilizcede ise tek seferlik somut bir başarıyı olumlu cümlede 'I could do it' ile anlatamazsınız; 'I was able to do it' veya 'I managed to do it' demek zorundasınız.",
        "rules": [
            {
                "name": "One-Time Success Limitation",
                "pattern": "Affirmative specific achievement -> was/were able to OR managed to (NOT could)",
                "use_cases": [
                    "Reporting quarterly accomplishments, solved incidents, and completed deliverables",
                    "Describing travel delays overcome and difficult logistical successes"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Could for Specific Achievement Trap",
                "description_tr": "Tek seferlik bir krizi çözdüğünü anlatırken olumlu cümlede 'could' kullanmak yanlış bir kullanım yaratır.",
                "trap_example": "Although the traffic was horrible, I could reach the airport on time.",
                "correction": "Although the traffic was horrible, I was able to reach the airport on time.",
                "key_difference_tr": "Belirli tekil bir başarıda olumlu 'could' kullanılmaz, 'was able to' kullanılır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "could (General past ability)",
                "concept_b": "was able to (Specific past success)",
                "difference_en": "'Could' denotes an enduring capability; 'was able to' denotes a verified triumph on a specific occasion.",
                "difference_tr": "'Could' genel bir yeteneği; 'was able to' belirli bir olaydaki fiili başarıyı gösterir.",
                "example_a": "In college, I could work through the entire night without feeling tired.",
                "example_b": "Yesterday, after three hours of debugging, I was able to fix the memory leak."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "The firewall failed, but we could prevent any data loss.",
                "correct": "The firewall failed, but we were able to prevent any data loss.",
                "explanation_en": "Overcoming a specific event successfully requires 'were able to' or 'managed to'.",
                "explanation_tr": "Belirli bir kriz anında veri kaybını önleme başarısı 'were able to' ile anlatılır."
            }
        ],
        "examples": [
            {
                "en": "Despite severe weather delays, the speaker was able to deliver the keynote address.",
                "tr": "Ağır hava koşulları kaynaklı gecikmelere rağmen, konuşmacı açılış konuşmasını yapabildi.",
                "context": "Conference event report",
                "register": "formal_written",
                "highlighted_phrase": "was able to deliver"
            },
            {
                "en": "When our lead architect lived in Tokyo, she could speak Japanese conversationally.",
                "tr": "Baş mimarımız Tokyo'da yaşarken günlük düzeyde Japonca konuşabiliyordu.",
                "context": "Colleague biography detail",
                "register": "neutral_workplace",
                "highlighted_phrase": "could speak"
            },
            {
                "en": "By collaborating closely, the team managed to launch the product ahead of schedule.",
                "tr": "Ekip, yakın iş birliği sayesinde ürünü takvimden önce piyasaya sürmeyi başardı.",
                "context": "Project retrospective highlights",
                "register": "neutral_workplace",
                "highlighted_phrase": "managed to launch"
            },
            {
                "en": "We couldn't connect to the remote server because the VPN gateway was down.",
                "tr": "VPN ağ geçidi kapalı olduğu için uzak sunucuya bağlanamadık.",
                "context": "IT troubleshooting ticket",
                "register": "neutral_workplace",
                "highlighted_phrase": "couldn't connect"
            }
        ],
        "topic_tags": ["work-career", "problem-solving"]
    },
    {
        "id": "grammar.b1.connecting-adverbs-however-moreover-therefore",
        "title": "Transitions in Written English: 'However, Therefore, Moreover, and In Addition'",
        "cefr_level": "B1",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Transition adverbs link independent sentences logically, showing contrast (however), consequence (therefore), or additive reinforcement (moreover, in addition), and require distinct punctuation.",
        "summary_tr": "Geçiş zarfları (However, Therefore, Moreover, In addition), bağımsız cümleleri mantıksal olarak bağlar; zıtlık, sonuç veya ekleme bildirir ve virgülle ayrılan özel noktalama gerektirir.",
        "explanation_en": [
            {
                "title": "Sentence Adverbs and Punctuation Conventions",
                "content": "Unlike coordinating conjunctions (but, so, and) which connect clauses within a single sentence, transition adverbs typically begin a new sentence followed immediately by a comma, or follow a semicolon. 'However' introduces a counter-point or limitation; 'Therefore' introduces a logical result; 'Moreover' and 'In addition' introduce supplementary supporting arguments.",
                "patterns": [
                    "Sentence start: Transition Adverb + Comma + Subject + Verb (e.g., However, costs increased.)",
                    "Semicolon linking: Clause 1; Transition Adverb, Clause 2 (e.g., Sales dropped; therefore, we adapted.)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Ancak', 'Bu nedenle', 'Dahası' bağlaçlarıdır. En yaygın hata, 'however' sözcüğünü 'but' gibi iki cümleyi sadece virgülle bağlayan bir bağlaç sanmaktır. 'We were ready, however it rained' yanlıştır; ya nokta konulmalı ya da noktalı virgül kullanılmalıdır.",
        "rules": [
            {
                "name": "Transition Punctuation Integrity",
                "pattern": "Period + Transition + Comma OR Semicolon + Transition + Comma",
                "use_cases": [
                    "Writing professional emails, project status reports, and business evaluations",
                    "Structuring clear analytical essays and formal arguments"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Comma Splice with However Trap",
                "description_tr": "Türkçede 'fakat/ancak' öncesinde virgül yetebilirken, İngilizcede iki bağımsız cümleyi 'however' ile bağlarken sadece virgül kullanmak (comma splice) büyük bir hatadır.",
                "trap_example": "The prototype showed promising results, however more testing is required.",
                "correction": "The prototype showed promising results. However, more testing is required.",
                "key_difference_tr": "'However' bir bağlaç değil geçiş zarfıdır; yeni bir cümle başlatır ve ardından virgül alır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "However (Contrast / limitation)",
                "concept_b": "Therefore (Consequence / outcome)",
                "difference_en": "'However' qualifies or contradicts the previous statement; 'therefore' presents the direct deductive result.",
                "difference_tr": "'However' zıtlık ve kısıtlama, 'therefore' ise mantıksal sonuç bildirir.",
                "example_a": "The initial cost is high. However, long-term savings are substantial.",
                "example_b": "The battery degraded significantly; therefore, the unit must be replaced."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "The feature is useful, moreover it is free.",
                "correct": "The feature is useful. Moreover, it is free. / ...useful; moreover, it is free.",
                "explanation_en": "'Moreover' cannot connect two complete sentences with a mere comma.",
                "explanation_tr": "'Moreover' geçiş zarfıdır, iki bağımsız cümleyi sadece virgülle bağlayamaz."
            }
        ],
        "examples": [
            {
                "en": "The proposed timeline is ambitious. However, the engineering team has the necessary resources.",
                "tr": "Önerilen takvim iddialı. Ancak, mühendislik ekibi gerekli kaynaklara sahip.",
                "context": "Executive project proposal review",
                "register": "formal_written",
                "highlighted_phrase": "However, the engineering team"
            },
            {
                "en": "Quarterly revenues exceeded our forecasts; therefore, we can accelerate hiring.",
                "tr": "Üç aylık gelirler tahminlerimizi aştı; bu nedenle, işe alımları hızlandırabiliriz.",
                "context": "Financial quarterly update",
                "register": "formal_written",
                "highlighted_phrase": "therefore, we can"
            },
            {
                "en": "The new framework improves security. Moreover, it reduces deployment latency by twenty percent.",
                "tr": "Yeni çatı güvenliği artırıyor. Dahası, dağıtım gecikmesini yüzde yirmi azaltıyor.",
                "context": "Technical architecture evaluation",
                "register": "formal_written",
                "highlighted_phrase": "Moreover, it reduces"
            },
            {
                "en": "In addition, all external contractors must complete security training before gaining system access.",
                "tr": "Ek olarak, tüm dış yükleniciler sistem erişimi elde etmeden önce güvenlik eğitimini tamamlamalıdır.",
                "context": "Compliance policy memorandum",
                "register": "formal_written",
                "highlighted_phrase": "In addition, all"
            }
        ],
        "topic_tags": ["business", "communication"]
    },
    {
        "id": "grammar.b1.unless-as-long-as-provided-that",
        "title": "Conditional Connectors: 'Unless', 'As long as', and 'Provided that'",
        "cefr_level": "B1",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "'Unless' means 'except if' or 'if not', while 'as long as' and 'provided that' express necessary positive conditions, establishing boundaries in business agreements.",
        "summary_tr": "'Unless' (-medikçe / -mezse) 'if not' anlamına gelirken; 'as long as' ve 'provided that' (-dığı sürece / şartıyla) olumlu ön koşulları belirtir.",
        "explanation_en": [
            {
                "title": "Conditional Boundary Markers",
                "content": "'Unless' already contains a negative meaning ('if you don't attend' = 'unless you attend'). Therefore, the verb inside the 'unless' clause is almost always affirmative. 'As long as' and 'provided (that)' establish enabling conditions: 'You can work remotely as long as you meet your deadlines'. Like standard conditionals, future tenses are not used in the conditional clause.",
                "patterns": [
                    "Unless + Affirmative Verb, will / modal (e.g., Unless we act, we will lose market share)",
                    "As long as + Present Simple, will / can (e.g., As long as tests pass, you can deploy)",
                    "Provided that + Present Simple, will / can (e.g., Provided that approval is granted, we will proceed)"
                ]
            }
        ],
        "explanation_tr": "'Unless' kendi içinde olumsuzluk barındırdığı için yanındaki fiile 'don't' veya 'doesn't' eklenmez ('Unless you don't pay' denmez, 'Unless you pay' denir). 'Provided that' ise daha resmi sözleşmelerde '... olması koşuluyla' anlamında kullanılır.",
        "rules": [
            {
                "name": "Unless Non-Negative Clause Rule",
                "pattern": "Unless + Subject + Affirmative Verb (meaning: If Subject does not Verb)",
                "use_cases": [
                    "Defining service level agreements, terms of service, and conditional contracts",
                    "Setting deadlines, compliance rules, and operational requirements"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Double Negative with Unless Trap",
                "description_tr": "Türkçe düşünürken 'yapmadıkça' ekindeki olumsuzluğu çevirip 'Unless you don't...' şeklinde çifte olumsuzluk yapmak yaygın bir hatadır.",
                "trap_example": "Unless you don't submit the timesheet by Friday, payment will be delayed.",
                "correction": "Unless you submit the timesheet by Friday, payment will be delayed.",
                "key_difference_tr": "'Unless' zaten olumsuzluk anlamı taşıdığı için cümlecikteki fiil olumlu kurulur."
            }
        ],
        "contrasts": [
            {
                "concept_a": "unless (Negative exception)",
                "concept_b": "provided that (Positive condition)",
                "difference_en": "'Unless' emphasizes what happens if a condition is NOT met; 'provided that' emphasizes the condition that enables success.",
                "difference_tr": "'Unless' olmazsa ne olacağını, 'provided that' ise ne koşulda gerçekleşeceğini vurgular.",
                "example_a": "We cannot sign the contract unless legal approves it.",
                "example_b": "We will sign the contract provided that legal approves it."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "As long as you will submit the report, everything is fine.",
                "correct": "As long as you submit the report, everything is fine.",
                "explanation_en": "Conditional clauses with 'as long as' take the Present Simple for future references, not 'will'.",
                "explanation_tr": "'As long as' koşul cümleciğinde gelecek zaman anlamı için 'will' değil, geniş zaman kullanılır."
            }
        ],
        "examples": [
            {
                "en": "We will miss the Q3 delivery deadline unless we onboard two additional backend engineers.",
                "tr": "İki ek arka yüz mühendisi istihdam etmedikçe 3. çeyrek teslimat tarihini kaçıracağız.",
                "context": "Resource planning meeting",
                "register": "neutral_workplace",
                "highlighted_phrase": "unless we onboard"
            },
            {
                "en": "You may take leave on Monday provided that your current deliverables are completed.",
                "tr": "Mevcut teslimatlarınız tamamlanmış olması şartıyla pazartesi günü izin kullanabilirsiniz.",
                "context": "Manager approving time-off request",
                "register": "formal_written",
                "highlighted_phrase": "provided that your current"
            },
            {
                "en": "The subscription will renew automatically as long as the payment method remains valid.",
                "tr": "Ödeme yöntemi geçerli kaldığı sürece abonelik otomatik olarak yenilenecektir.",
                "context": "SaaS billing terms",
                "register": "neutral_workplace",
                "highlighted_phrase": "as long as the payment"
            },
            {
                "en": "Unless management intervenes, the two departments will continue duplicating technical work.",
                "tr": "Yönetim müdahale etmezse iki departman teknik işleri mükerrer yapmaya devam edecek.",
                "context": "Organizational efficiency audit",
                "register": "neutral_workplace",
                "highlighted_phrase": "Unless management intervenes"
            }
        ],
        "topic_tags": ["business", "work-career"]
    },
    {
        "id": "grammar.b1.indirect-polite-questions",
        "title": "Indirect Questions: Polite Word Order in Workplace Inquiries",
        "cefr_level": "B1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Indirect questions soften inquiries by embedding them inside introductory phrases ('Could you tell me...', 'Do you know...'); embedded clauses revert to statement word order.",
        "summary_tr": "Dolaylı sorular (Indirect Questions), soru ifadelerini 'Could you tell me...', 'Do you know...' gibi giriş kalıplarıyla yumuşatır ve içteki cümlecik normal düz cümle kelime sırasına döner.",
        "explanation_en": [
            {
                "title": "Embedded Inversion Reversal",
                "content": "In a direct question, the auxiliary verb comes before the subject ('Where is the printer?', 'What time does the train arrive?'). In an indirect question, because the introductory clause already provides the interrogative structure, the embedded clause uses standard affirmative word order (Subject + Verb), and auxiliary 'do/does/did' disappears completely. Yes/No questions use 'if' or 'whether'.",
                "patterns": [
                    "Wh-question: Could you tell me + Wh-word + Subject + Verb? (e.g., Could you tell me where the station is?)",
                    "Yes/No question: Do you know + if / whether + Subject + Verb? (e.g., Do you know if the store is open?)"
                ]
            }
        ],
        "explanation_tr": "En kritik kural: Dolaylı soruda içteki cümlede soru dizilimi yapılmaz, düz cümle kurulur. 'Could you tell me where is the station?' yanlıştır; 'where the station IS' denmelidir. 'Do/does/did' yardımcı fiilleri de iç cümleden tamamen atılır ('Do you know when the meeting starts?').",
        "rules": [
            {
                "name": "Embedded Statement Word Order Rule",
                "pattern": "Introductory phrase + Wh-word / if / whether + Subject + Verb (NO do/does/did)",
                "use_cases": [
                    "Asking polite questions to clients, executives, or unfamiliar colleagues",
                    "Inquiring about directions, schedules, and administrative procedures"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Direct Inversion Retention Trap",
                "description_tr": "Giriş nezaket kalıbından sonra soru olduğunu düşünüp yardımcı fiili başa almaya devam etmek ('where did he go' demek) en yaygın hatadır.",
                "trap_example": "Could you tell me where did the director go?",
                "correction": "Could you tell me where the director went?",
                "key_difference_tr": "İçteki cümlecik soru değil düz cümle yapısına döner: 'where' ardından özne ve geçmiş zaman fiili gelir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Direct Question (Blunt / casual)",
                "concept_b": "Indirect Question (Polite / professional)",
                "difference_en": "Direct questions invert auxiliary and subject; indirect questions embed the question as a statement clause.",
                "difference_tr": "Doğrudan soru yardımcı fiili öne alır; dolaylı soru nezaket kalıbı sonrası düz cümle sırası izler.",
                "example_a": "When will the deployment finish?",
                "example_b": "Could you let me know when the deployment will finish?"
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "Do you know what time does the office open?",
                "correct": "Do you know what time the office opens?",
                "explanation_en": "Auxiliary 'does' is removed, and the verb takes standard third-person 's'.",
                "explanation_tr": "Dolaylı soruda 'does' atılır ve fiile normal geniş zaman '-s' takısı eklenir."
            }
        ],
        "examples": [
            {
                "en": "Could you tell me where the security badge office is located?",
                "tr": "Güvenlik kartı ofisinin nerede olduğunu söyleyebilir misiniz?",
                "context": "Visitor asking at corporate lobby",
                "register": "formal_written",
                "highlighted_phrase": "where the security badge office is located"
            },
            {
                "en": "I was wondering if the marketing team has reviewed the press release.",
                "tr": "Pazarlama ekibinin basın bültenini inceleyip incelemediğini merak ediyordum.",
                "context": "Polite project inquiry email",
                "register": "formal_written",
                "highlighted_phrase": "if the marketing team has reviewed"
            },
            {
                "en": "Do you happen to know when the next release window opens?",
                "tr": "Bir sonraki yayın penceresinin ne zaman açıldığını biliyor musunuz acaba?",
                "context": "Chat message to a release manager",
                "register": "neutral_workplace",
                "highlighted_phrase": "when the next release window opens"
            },
            {
                "en": "Could you explain why this database query is taking several seconds to execute?",
                "tr": "Bu veritabanı sorgusunun neden birkaç saniye sürdüğünü açıklayabilir misiniz?",
                "context": "Engineering peer code review discussion",
                "register": "neutral_workplace",
                "highlighted_phrase": "why this database query is taking"
            }
        ],
        "topic_tags": ["communication", "work-career"]
    },
    {
        "id": "grammar.b1.defining-relative-clauses-omission-of-pronoun",
        "title": "Contact Clauses: Omission of Relative Pronouns in Defining Relative Clauses",
        "cefr_level": "B1",
        "category": "relative_and_participle_clauses",
        "summary_en": "In defining relative clauses, the relative pronoun (who, which, that) can be omitted when it functions as the object of the relative clause, creating fluent, natural English.",
        "summary_tr": "Tanımlayıcı sıfat cümleciklerinde (defining relative clauses), bağlaç zamir (who, which, that) yan cümlenin nesnesi konumundaysa cümleden atılabilir (Contact Clause).",
        "explanation_en": [
            {
                "title": "Subject Pronoun vs. Object Pronoun in Relative Clauses",
                "content": "When the relative pronoun is followed immediately by a verb, it is the subject of the clause and CANNOT be omitted: 'The engineer WHO wrote this code has left'. However, when the relative pronoun is followed by another subject + verb, it is the object and can be omitted naturally: 'The code [that] the engineer wrote works perfectly'. Omission is standard in modern spoken and business English.",
                "patterns": [
                    "Subject relative (NEVER omit): Noun + who/which/that + Verb (e.g., The bug that caused the crash)",
                    "Object relative (FREELY omit): Noun + (who/which/that) + Subject + Verb (e.g., The bug [that] we fixed)"
                ]
            }
        ],
        "explanation_tr": "Türkçede sıfat fiillerle (-dığı, -en) tek bir yapıyla çözülür ('yazdığımız kod'). İngilizcede bağlacın atılabilmesi için yan cümlenin kendi öznesi olmalıdır. Eğer bağlaç eylemi yapan kişinin kendisiyse ('the person who called') atılamaz; eylemin nesnesiyse ('the person I called') 'who/whom' atılabilir.",
        "rules": [
            {
                "name": "Object Relative Pronoun Deletion Rule",
                "pattern": "Noun + (relative pronoun omitted) + Subject + Verb",
                "use_cases": [
                    "Drafting natural, concise business emails and conversational explanations",
                    "Avoiding heavy, repetitive usage of 'that' and 'which' in technical prose"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Subject Pronoun Deletion Trap",
                "description_tr": "Zamir atmayı öğrenen öğrencilerin özne konumundaki 'who' veya 'that' zamirini de atıp anlamsız cümleler kurması ('The person called me' gibi) ciddi bir hatadır.",
                "trap_example": "The specialist diagnosed the server issue has gone home.",
                "correction": "The specialist WHO diagnosed the server issue has gone home.",
                "key_difference_tr": "Eğer zamirden hemen sonra fiil geliyorsa, o zamir öznedir ve asla atılamaz."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Subject Pronoun (Mandatory)",
                "concept_b": "Object Pronoun (Optional / Elliptical)",
                "difference_en": "Subject pronouns are structurally indispensable; object pronouns can be omitted without loss of clarity.",
                "difference_tr": "Özne zamirleri vazgeçilmezdir; nesne zamirleri akıcılık için rahatlıkla atılabilir.",
                "example_a": "The vendor who supplied the routers offered a replacement.",
                "example_b": "The routers (which) the vendor supplied were defective."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "The report you requested it yesterday is ready on your desk.",
                "correct": "The report you requested yesterday is ready on your desk.",
                "explanation_en": "Do not keep the pronoun 'it' in the relative clause when 'the report' is already the object.",
                "explanation_tr": "Sıfat cümleciğinde nesne zamiri atıldığında cümlecik içine gereksiz bir 'it' zamiri eklenmez."
            }
        ],
        "examples": [
            {
                "en": "The presentation you delivered at the conference received outstanding feedback.",
                "tr": "Konferansta yaptığınız sunum olağanüstü geri bildirimler aldı.",
                "context": "Manager congratulating a team member",
                "register": "neutral_workplace",
                "highlighted_phrase": "The presentation you delivered"
            },
            {
                "en": "Is this the candidate we interviewed during the morning panel?",
                "tr": "Sabahki panelde mülakat yaptığımız aday bu mu?",
                "context": "Hiring manager discussing applicant list",
                "register": "neutral_workplace",
                "highlighted_phrase": "the candidate we interviewed"
            },
            {
                "en": "We must verify the credentials that authenticate third-party API requests.",
                "tr": "Üçüncü taraf API isteklerini doğrulayan kimlik bilgilerini kontrol etmeliyiz.",
                "context": "Security audit note (Subject pronoun cannot be omitted)",
                "register": "neutral_workplace",
                "highlighted_phrase": "credentials that authenticate"
            },
            {
                "en": "The feedback our team received helped us refine the mobile checkout process.",
                "tr": "Ekibimizin aldığı geri bildirimler, mobil ödeme sürecini iyileştirmemize yardımcı oldu.",
                "context": "Product sprint retrospective",
                "register": "neutral_workplace",
                "highlighted_phrase": "The feedback our team received"
            }
        ],
        "topic_tags": ["work-career", "communication"]
    },
    {
        "id": "grammar.b1.used-to-vs-be-get-used-to",
        "title": "Habitual Contrasts: 'Used to do' vs. 'Be / Get used to doing'",
        "cefr_level": "B1",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "'Used to + infinitive' refers to past states and habits that are no longer true, whereas 'be/get used to + noun/-ing' expresses familiarity and adaptation to something normal or challenging.",
        "summary_tr": "'Used to + yalın fiil' artık geçerli olmayan geçmiş alışkanlıkları ifade ederken; 'be/get used to + fiilimsi (-ing)' bir duruma alışkın olmayı veya alışma sürecini anlatır.",
        "explanation_en": [
            {
                "title": "Past Habit vs. Present Familiarity",
                "content": "'Used to + bare verb' exists only in the past tense ('I used to work in London, but now I work in Berlin'). By contrast, 'be used to + -ing' means that something is familiar and no longer strange ('I am used to waking up early'). 'Get used to + -ing' emphasizes the dynamic process of becoming accustomed to something new ('You will get used to the new software'). Here 'to' is a preposition, which is why it requires a gerund.",
                "patterns": [
                    "Past discontinued habit: Subject + used to + Base Verb (e.g., We used to share an office)",
                    "Present familiarity: Subject + be + used to + Verb-ing / Noun (e.g., She is used to tight deadlines)",
                    "Process of adapting: Subject + get + used to + Verb-ing / Noun (e.g., You will get used to the noise)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'eskiden yapardım' (used to) ile 'yapmaya alışkınım' (be used to) ve 'yapmaya alışıyorum' (get used to) kavramları tamamen ayrıdır. Türk öğrencilerin en sık düştüğü tuzak, 'I am used to wake up early' diyerek 'to' edatından sonra yalın fiil getirmeleridir. 'Be used to'daki 'to' bir edattır, fiil mutlaka -ing almalıdır.",
        "rules": [
            {
                "name": "Used to Pattern Divergence",
                "pattern": "used to + BASE verb vs. be/get used to + GERUND (-ing) / Noun",
                "use_cases": [
                    "Contrasting previous career roles with current working practices",
                    "Describing cultural, ergonomic, or procedural adaptation in international teams"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Be Used to with Bare Infinitive Trap",
                "description_tr": "'Be used to' yapısından sonra alışkanlık gereği yalın fiil kullanmak çok yaygın bir gramer hatasıdır.",
                "trap_example": "I am working in an international team, so I am used to speak English daily.",
                "correction": "I am working in an international team, so I am used to SPEAKING English daily.",
                "key_difference_tr": "'Be used to' kalıbındaki 'to' yönelme edatıdır; kendisinden sonra fiilimsi (-ing) veya isim gelir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "used to do (Past discontinued habit)",
                "concept_b": "be used to doing (Current familiarity)",
                "difference_en": "'Used to do' means the action has stopped; 'be used to doing' means the action is customary and comfortable now.",
                "difference_tr": "'Used to do' geçmişte kalmış eylemi, 'be used to doing' ise şu anda alışılmış durumu anlatır.",
                "example_a": "I used to travel abroad every month before the restructuring.",
                "example_b": "I am used to working across different time zones."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "It took me three months to used to the open-plan office.",
                "correct": "It took me three months to get used to the open-plan office.",
                "explanation_en": "The transition process of becoming accustomed requires the verb 'get used to'.",
                "explanation_tr": "Bir duruma alışma sürecini anlatırken 'get used to' fiili kullanılır."
            }
        ],
        "examples": [
            {
                "en": "Our department used to deploy code manually before we automated the CI/CD pipeline.",
                "tr": "Departmanımız CI/CD hattını otomatikleştirmeden önce kodları manuel olarak dağıtırdı.",
                "context": "Engineering retrospective discussion",
                "register": "neutral_workplace",
                "highlighted_phrase": "used to deploy"
            },
            {
                "en": "Remote team members are used to collaborating asynchronously across different continents.",
                "tr": "Uzaktan çalışan ekip üyeleri farklı kıtalar arasında asenkron iş birliği yapmaya alışkındır.",
                "context": "Global team culture description",
                "register": "neutral_workplace",
                "highlighted_phrase": "are used to collaborating"
            },
            {
                "en": "New employees usually need a few weeks to get used to our project management tools.",
                "tr": "Yeni çalışanların proje yönetimi araçlarımıza alışması genellikle birkaç hafta sürer.",
                "context": "Onboarding orientation meeting",
                "register": "neutral_workplace",
                "highlighted_phrase": "get used to our project"
            },
            {
                "en": "She used to lead the design team, but she recently transferred to product management.",
                "tr": "Eskiden tasarım ekibine liderlik ediyordu, ancak yakın zamanda ürün yönetimine geçti.",
                "context": "Internal personnel transition announcement",
                "register": "neutral_workplace",
                "highlighted_phrase": "used to lead"
            }
        ],
        "topic_tags": ["work-career", "daily-life"]
    }
]
