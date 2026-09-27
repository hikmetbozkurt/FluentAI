#!/usr/bin/env python3
"""
Grammar Batch 003: B2 Lessons (13 lessons).
"""

from typing import List, Dict, Any

B2_LESSONS: List[Dict[str, Any]] = [
    {
        "id": "grammar.b2.inversion-with-conditional-if-omission",
        "title": "Conditional Inversion without 'If': 'Should you require' and 'Were we to agree'",
        "cefr_level": "B2",
        "category": "inversion_and_emphasis",
        "summary_en": "In formal and business registers, 'if' can be omitted in first and second conditional sentences by inverting the auxiliary 'Should' or subjunctive 'Were' with the subject.",
        "summary_tr": "Resmi ve ticari yazışmalarda 'if' atılarak, 'Should' veya 'Were' yardımcı fiilinin özneyle devrik (inverted) yapılması yoluyla daha diplomatik ve prestijli koşul cümleleri kurulur.",
        "explanation_en": [
            {
                "title": "Formal Conditional Omission Rules",
                "content": "In Type 1 conditionals, replacing 'If you need' with 'Should you require / need' creates a polished formal tone common in corporate communication and customer correspondence. In Type 2 conditionals, replacing 'If we decided' with 'Were we to decide' creates a polite hypothetical distancing. If the verb is 'be', invert directly: 'Were I in your position'. In negative inverted clauses, 'not' comes after the subject ('Should you NOT wish to renew').",
                "patterns": [
                    "Type 1 Inversion: Should + Subject + Base Verb, will/can/imperative (e.g., Should you have questions, please call)",
                    "Type 2 Inversion (Action): Were + Subject + to-infinitive, would (e.g., Were they to accept, we would sign)",
                    "Type 2 Inversion (State): Were + Subject + Complement, would (e.g., Were the budget available, we would proceed)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Gereksinim duymanız halinde...' veya 'Kabul edecek olursak...' nezaket ifadeleridir. En büyük tuzak, 'Should' yardımcı fiilini zorunluluk (meli/malı) anlamıyla karıştırmaktır; buradaki 'Should' sadece 'eğer olur da...' anlamında bir koşul aracıdır. Cümle soru cümlesi değil, sonu noktayla biten devrik bir koşuldur.",
        "rules": [
            {
                "name": "Should / Were Conditional Inversion Pattern",
                "pattern": "Should + Subj + Base Verb OR Were + Subj + to + Base Verb (NO if, NO question mark)",
                "use_cases": [
                    "Drafting formal executive proposals, legal disclaimers, and corporate customer communications",
                    "Softening commercial terms, hypothetical bargaining scenarios, and contingent commitments"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Should as Obligation Trap",
                "description_tr": "'Should you need help' ifadesini 'Yardıma ihtiyacın olmalı' şeklinde tercüme etmek büyük bir yanılgıdır; bu tamamen 'If you need help' (Yardıma ihtiyacınız olursa) demektir.",
                "trap_example": "Should the server crash, you must restart the backup container.",
                "correction": "Should the server crash, you must restart the backup container. (Meaning: If the server crashes)",
                "key_difference_tr": "Devrik cümlenin başındaki 'Should' bir tavsiye/zorunluluk değil, 'if' bağlacının resmi karşılığıdır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "If you require assistance (Standard)",
                "concept_b": "Should you require assistance (Formal diplomatic)",
                "difference_en": "Both convey identical conditional logic, but 'Should you require' elevates register and conveys executive deference.",
                "difference_tr": "Her ikisi de aynı koşulu bildirir; ancak 'Should you require' çok daha resmi ve profesyonel bir tona sahiptir.",
                "example_a": "If you have any further questions, please contact our support desk.",
                "example_b": "Should you have any further questions, please do not hesitate to contact our office."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "Should you will need further clarification, please inform us.",
                "correct": "Should you need further clarification, please inform us.",
                "explanation_en": "Do not include 'will' inside the inverted conditional clause.",
                "explanation_tr": "Devrik koşul cümleciğinin içinde asla 'will' kullanılmaz; fiil yalın kalır."
            }
        ],
        "examples": [
            {
                "en": "Should you encounter any authentication errors, please alert the security operations center.",
                "tr": "Herhangi bir kimlik doğrulama hatasıyla karşılaşacak olursanız, lütfen güvenlik operasyon merkezini uyarınız.",
                "context": "Enterprise security policy document",
                "register": "formal_written",
                "highlighted_phrase": "Should you encounter"
            },
            {
                "en": "Were we to expand into the Southeast Asian market, we would need local distribution partners.",
                "tr": "Güneydoğu Asya pazarına genişleyecek olsaydık, yerel dağıtım ortaklarına ihtiyacımız olurdu.",
                "context": "Strategic board planning discussion",
                "register": "formal_written",
                "highlighted_phrase": "Were we to expand"
            },
            {
                "en": "Should any participant not be able to attend in person, a secure video link will be provided.",
                "tr": "Herhangi bir katılımcı bizzat katılamayacak olursa, güvenli bir video bağlantısı sağlanacaktır.",
                "context": "Annual shareholder meeting logistics notice",
                "register": "formal_written",
                "highlighted_phrase": "Should any participant not be able"
            },
            {
                "en": "Were the initial deployment to fail, our automated rollback mechanism would restore the previous build.",
                "tr": "İlk dağıtım başarısız olacak olursa, otomatik geri alma mekanizmamız önceki sürümü geri yüklerdi.",
                "context": "DevOps contingency architecture documentation",
                "register": "formal_written",
                "highlighted_phrase": "Were the initial deployment to fail"
            }
        ],
        "topic_tags": ["business", "communication"]
    },
    {
        "id": "grammar.b2.future-in-the-past-was-going-to-was-to",
        "title": "Future in the Past: Unfulfilled Intentions and Predetermined Events ('Was going to' vs. 'Was to')",
        "cefr_level": "B2",
        "category": "tenses_and_aspect",
        "summary_en": "Future in the past expresses plans, intentions, or destiny viewed from a past vantage point; 'was going to' often highlights unfulfilled intentions, while 'was to' denotes formal destiny or official schedule.",
        "summary_tr": "Geçmişteki Gelecek (Future in the Past), geçmiş bir andan ileriye bakılarak planlanan eylemleri anlatır; 'was going to' gerçekleşmeyen niyetleri, 'was to' ise resmi planları veya kaçınılmaz akıbeti belirtir.",
        "explanation_en": [
            {
                "title": "Retrospective Intentions and Destiny",
                "content": "When narrating past events, we often need to mention what was scheduled or intended next. 'Was / were going to + verb' usually signals that an intention was interrupted or abandoned ('I was going to call you, but the meeting ran late'). 'Would + verb' functions as the past equivalent of 'will' in reported perspectives. The formal structure 'was / were to + base verb' denotes official arrangements or retrospective destiny ('He was to become the company's youngest CEO').",
                "patterns": [
                    "Unfulfilled intention: Subject + was/were going to + Base Verb (e.g., We were going to launch in May)",
                    "Past prediction/promise: Subject + would + Base Verb (e.g., She promised she would send the logs)",
                    "Formal fate / schedule: Subject + was/were to + Base Verb (e.g., The team was to present on Monday)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'yapacaktım / yapacaktık (ama yapamadık)' yapısıdır. 'I was going to email you' dendiğinde dinleyici otomatik olarak araya bir engelin girdiğini ve e-postanın gönderilemediğini anlar. 'Was to' ise resmi biyografilerde veya iş takvimlerinde 'yapacaktı / olması mukadderdi' anlamında kullanılır.",
        "rules": [
            {
                "name": "Past Perspective Future Pattern",
                "pattern": "Subject + was/were going to + Base Verb OR Subject + was/were to + Base Verb",
                "use_cases": [
                    "Explaining project schedule shifts, canceled features, and root causes of delays",
                    "Documenting corporate history, strategic realignments, and planned milestones"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Using Would Instead of Was Going To for Canceled Plans",
                "description_tr": "Planlanıp gerçekleşmeyen bir eylemi anlatırken 'I would do' demek şart kipine kayar; gerçekleşmeyen niyet açıkça 'I was going to do' ile ifade edilmelidir.",
                "trap_example": "I would attend the steering committee, but my train was cancelled.",
                "correction": "I was going to attend the steering committee, but my train was cancelled.",
                "key_difference_tr": "Geçmişte planlanan ama bozulan niyetler 'would' ile değil 'was going to' ile kurulur."
            }
        ],
        "contrasts": [
            {
                "concept_a": "was going to do (Unfulfilled intention)",
                "concept_b": "was to do (Formal scheduled occurrence / destiny)",
                "difference_en": "'Was going to' implies the plan did not happen; 'was to' formalizes what was destined or scheduled to occur.",
                "difference_tr": "'Was going to' çoğunlukla iptal olan planı, 'was to' ise resmi takvimi veya kaçınılmaz akıbeti belirtir.",
                "example_a": "We were going to announce the merger, but negotiations stalled.",
                "example_b": "The committee was to convene at noon, as mandated by the bylaws."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "They were to have delivered the report yesterday, but they forgot.",
                "correct": "They were supposed to deliver the report yesterday / They were going to deliver the report yesterday...",
                "explanation_en": "Use 'were going to' or 'were supposed to' for missed mundane workplace deadlines.",
                "explanation_tr": "Günlük iş gecikmelerinde 'were going to' veya 'were supposed to' daha doğaldır."
            }
        ],
        "examples": [
            {
                "en": "We were going to release the patch on Tuesday, but QA discovered a critical memory leak.",
                "tr": "Yamayı salı günü yayınlayacaktık, ancak kalite kontrol ekibi kritik bir bellek sızıntısı keşfetti.",
                "context": "Sprint review delay explanation",
                "register": "neutral_workplace",
                "highlighted_phrase": "were going to release"
            },
            {
                "en": "The lead researcher was to present her findings in Geneva before the conference was canceled.",
                "tr": "Baş araştırmacı, konferans iptal edilmeden önce bulgularını Cenevre'de sunacaktı.",
                "context": "Academic research grant report",
                "register": "formal_written",
                "highlighted_phrase": "was to present"
            },
            {
                "en": "I was going to discuss this with human resources, but the issue resolved itself.",
                "tr": "Bunu insan kaynaklarıyla görüşecektim, fakat sorun kendiliğinden çözüldü.",
                "context": "Informal peer dialogue in the office",
                "register": "informal_spoken",
                "highlighted_phrase": "was going to discuss"
            },
            {
                "en": "Little did the founders know that this modest prototype was to become a market-defining platform.",
                "tr": "Kurucuların, bu mütevazı prototipin sektörü tanımlayan bir platform haline geleceğinden haberleri bile yoktu.",
                "context": "Corporate history retrospective",
                "register": "formal_written",
                "highlighted_phrase": "was to become"
            }
        ],
        "topic_tags": ["work-career", "business"]
    },
    {
        "id": "grammar.b2.double-comparatives-the-more-the-better",
        "title": "Proportional Comparatives: Cause and Effect with 'The more..., the more...'",
        "cefr_level": "B2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Proportional comparatives use parallel comparative clauses preceded by 'the' to express how an increase or decrease in one factor causes a corresponding change in another.",
        "summary_tr": "Orantılı karşılaştırmalar ('The more..., the more...'), bir değişkendeki artış veya azalışın diğer değişkende doğrudan yol açtığı paralel etkiyi ifade eder ('Ne kadar..., o kadar...').",
        "explanation_en": [
            {
                "title": "Parallel Comparative Syntax",
                "content": "The structure pairs two clauses, each introduced by 'The' followed by a comparative adjective, adverb, or quantifier ('more', 'less', 'fewer', '-er' adjectives). In formal analysis, the verb in either clause can sometimes be omitted if clear from context ('The sooner, the better'). When subjects and verbs are included, word order remains 'The + comparative + subject + verb'.",
                "patterns": [
                    "Full clause: The + comparative + Subject + Verb, the + comparative + Subject + Verb (e.g., The more data we collect, the more accurate the model becomes)",
                    "Elliptical phrase: The + comparative, the + comparative (e.g., The earlier, the better)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Ne kadar erken başlarsak, o kadar çabuk bitiririz' yapısıdır. En büyük hata, İngilizce cümlelerin başında 'the' kullanmayı unutmak veya ikinci kısımda devrik kelime sırası yapmaya çalışmaktır. Her iki tarafın başında da mutlaka 'the' yer almalıdır.",
        "rules": [
            {
                "name": "Symmetric Proportional Comparative Rule",
                "pattern": "The + Comparative (+ Subj + Verb), The + Comparative (+ Subj + Verb)",
                "use_cases": [
                    "Describing business trade-offs, algorithmic complexity, and economic correlations",
                    "Formulating analytical principles in engineering, finance, and productivity"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Missing Second 'The' Trap",
                "description_tr": "Türkçe düşünürken 'ne kadar... o kadar...' dengesini bozup ikinci kısımda 'the' edatını düşürmek çok sık rastlanan bir hatadır.",
                "trap_example": "The more memory we allocate, better the performance becomes.",
                "correction": "The more memory we allocate, THE better the performance becomes.",
                "key_difference_tr": "Her iki karşılaştırma öbeği de başında mutlaka 'the' taşımak zorundadır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "As X increases, Y increases (Linear statement)",
                "concept_b": "The more X, the more Y (Rhetorical proportional emphasis)",
                "difference_en": "'As' clauses are standard narrative prose; 'The more... the more...' provides heightened thematic focus and tight rhetorical symmetry.",
                "difference_tr": "'As' yapısı düz anlatım sunarken; 'The more... the more...' kavramsal paralelliğe ve analitik vurguya odaklanır.",
                "example_a": "As team size expands, communication overhead grows proportionally.",
                "example_b": "The larger the engineering team, the more complex the communication overhead becomes."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "The more you study, you will understand more.",
                "correct": "The more you study, the more you will understand.",
                "explanation_en": "The second comparative must front immediately after 'the', not appear at the end of the clause.",
                "explanation_tr": "İkinci karşılaştırma zarfı cümlenin sonuna bırakılmaz, 'the' kelimesinden hemen sonra öne alınır."
            }
        ],
        "examples": [
            {
                "en": "The more thoroughly we audit our dependencies, the fewer security vulnerabilities we face.",
                "tr": "Bağımlılıklarımızı ne kadar kapsamlı denetlersek, o kadar az güvenlik açığıyla karşılaşırız.",
                "context": "Software security briefing",
                "register": "neutral_workplace",
                "highlighted_phrase": "The more thoroughly we audit"
            },
            {
                "en": "The higher the ambient temperature in the data center, the more power cooling systems consume.",
                "tr": "Veri merkezindeki ortam sıcaklığı ne kadar yüksek olursa, soğutma sistemleri o kadar fazla enerji tüketir.",
                "context": "Data center infrastructure analysis",
                "register": "formal_written",
                "highlighted_phrase": "The higher the ambient temperature"
            },
            {
                "en": "The sooner stakeholders provide feedback on the prototype, the faster we can iterate.",
                "tr": "Paydaşlar prototip hakkında ne kadar erken geri bildirim sağlarsa, o kadar hızlı yineleme yapabiliriz.",
                "context": "Design sprint collaboration message",
                "register": "neutral_workplace",
                "highlighted_phrase": "The sooner stakeholders provide"
            },
            {
                "en": "The more complex the legal wording in a contract, the harder it is for clients to parse.",
                "tr": "Bir sözleşmedeki hukuki dil ne kadar karmaşık olursa, müşterilerin bunu anlaması o kadar zorlaşır.",
                "context": "Corporate legal clarity presentation",
                "register": "neutral_workplace",
                "highlighted_phrase": "the harder it is"
            }
        ],
        "topic_tags": ["business", "science", "technology"]
    },
    {
        "id": "grammar.b2.participle-clauses-time-and-sequence",
        "title": "Participle Clauses of Time and Sequence: Present vs. Perfect Participles",
        "cefr_level": "B2",
        "category": "relative_and_participle_clauses",
        "summary_en": "Participle clauses replace adverbial time clauses to streamline prose; present participles (-ing) indicate simultaneous actions, while perfect participles (having + past participle) indicate completed prior actions.",
        "summary_tr": "Zaman bildiren ortaç cümlecikleri (Participle Clauses), zaman zarfı cümleciklerini kısaltır; 'V-ing' eşzamanlı eylemleri, 'having + V3' ise daha önce tamamlanmış öncelikli eylemleri ifade eder.",
        "explanation_en": [
            {
                "title": "Aspectual Differentiation in Participle Clauses",
                "content": "Participle clauses share the subject of the main clause. Use the present participle (-ing) when two actions happen simultaneously or immediately one after another: 'Reviewing the logs, the engineer noticed an anomaly'. Use the perfect participle ('Having + past participle') when it is vital to emphasize that the first action was fully completed before the second began: 'Having secured funding, the startup began hiring'. Using a dangling participle (where the implied subject differs from the main subject) is a major grammatical defect.",
                "patterns": [
                    "Simultaneous action: Verb-ing ..., Subject + Verb (e.g., Walking to work, I met John)",
                    "Prior completed action: Having + Past Participle ..., Subject + Verb (e.g., Having passed the tests, the build was deployed)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki '-erek/-arak' (yaparak) ve '-ip / yaptıktan sonra' (yapmış olarak) yapılarına denk gelir. En büyük tehlike 'dangling participle' (havada kalan ortaç) hatasıdır: Ortaç eylemini yapan özne ile ana cümlenin öznesi mutlaka aynı kişi/şey olmak zorundadır.",
        "rules": [
            {
                "name": "Subject Coreference and Temporal Aspect Rule",
                "pattern": "Having + V3 (prior action) vs. V-ing (simultaneous action); Subject of participle MUST match main clause subject",
                "use_cases": [
                    "Condensing incident reports, retrospective narratives, and technical process descriptions",
                    "Writing polished professional correspondence and executive briefing summaries"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Dangling Participle Transfer Trap",
                "description_tr": "Türkçede öznesi belirsiz ortaçlar kullanılabildiği için İngilizcede öznesi ana cümleyle uyuşmayan ortaçlar kurmak çok yaygındır.",
                "trap_example": "Having reviewed the financial audit, several discrepancies were found by the manager.",
                "correction": "Having reviewed the financial audit, the manager found several discrepancies.",
                "key_difference_tr": "Denetimi inceleyen 'discrepancies' (tutarsızlıklar) değil, 'manager' (yönetici) olduğu için ana cümlenin öznesi 'the manager' olmalıdır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Reviewing the logs (Simultaneous)",
                "concept_b": "Having reviewed the logs (Prior completion)",
                "difference_en": "'Reviewing' implies while looking through logs; 'Having reviewed' stresses that the log inspection was finished first.",
                "difference_tr": "'Reviewing' inceleme esnasında gerçekleşen durumu, 'Having reviewed' ise incelemenin tamamen bittiğini vurgular.",
                "example_a": "Scanning the room, the speaker noticed an empty chair in the front row.",
                "example_b": "Having delivered her keynote, the speaker took questions from the audience."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "After having finished the sprint, the team celebrated.",
                "correct": "Having finished the sprint, the team celebrated. / After finishing the sprint...",
                "explanation_en": "Do not combine the preposition 'after' with the perfect participle 'having finished'; choose one.",
                "explanation_tr": "'After' edatı ile 'having finished' kalıbı birlikte kullanılmaz; ya 'After finishing' ya da sadece 'Having finished' denir."
            }
        ],
        "examples": [
            {
                "en": "Having completed the compliance certification, the fintech platform expanded into Europe.",
                "tr": "Uyum sertifikasyonunu tamamlamış olan fintech platformu, Avrupa pazarına açıldı.",
                "context": "Fintech company milestone overview",
                "register": "formal_written",
                "highlighted_phrase": "Having completed the compliance"
            },
            {
                "en": "Investigating the unexpected spike in traffic, our network team detected a distributed denial-of-service attempt.",
                "tr": "Trafikteki beklenmeyen artışı araştıran ağ ekibimiz, dağıtık bir hizmet engelleme girişimini tespit etti.",
                "context": "Network security incident briefing",
                "register": "neutral_workplace",
                "highlighted_phrase": "Investigating the unexpected spike"
            },
            {
                "en": "Having exhausted all local troubleshooting options, the client escalated the issue to senior engineering.",
                "tr": "Tüm yerel sorun giderme seçeneklerini tüketmiş olan müşteri, sorunu kıdemli mühendislik ekibine iletti.",
                "context": "Support ticket escalation log",
                "register": "neutral_workplace",
                "highlighted_phrase": "Having exhausted all local"
            },
            {
                "en": "Opening the quarterly financial report, the chief executive highlighted our record subscription renewals.",
                "tr": "Üç aylık finansal raporu açan icra kurulu başkanı, rekor düzeydeki abonelik yenilemelerimize dikkat çekti.",
                "context": "Earnings call opening remarks",
                "register": "formal_written",
                "highlighted_phrase": "Opening the quarterly financial"
            }
        ],
        "topic_tags": ["work-career", "technology"]
    },
    {
        "id": "grammar.b2.cleft-sentences-it-is-it-was-basics",
        "title": "Focusing with It-Clefts: 'It was X that / who...'",
        "cefr_level": "B2",
        "category": "cleft_sentences",
        "summary_en": "It-cleft sentences isolate and emphasize a specific sentence element (subject, object, time, or prepositional phrase) using the formula 'It is / was + focused element + that / who'.",
        "summary_tr": "It-Cleft (Bölünmüş) cümleler, bir cümlenin belirli bir öğesini (özne, nesne, zaman veya edat öbeği) 'It is / was + vurgulanan öğe + that / who' yapısıyla öne çıkararak vurgulamaya yarar.",
        "explanation_en": [
            {
                "title": "Fronting the Focus Element",
                "content": "Instead of saying 'The marketing budget caused the deficit', an It-cleft splits the clause into two parts: 'It was THE MARKETING BUDGET that caused the deficit'. This focuses the listener's attention exclusively on the responsible agent or factor, ruling out alternatives. If a person is emphasized, both 'that' and 'who' are acceptable in modern English.",
                "patterns": [
                    "Focusing subject: It is/was + Subject Noun + that/who + Verb ... (e.g., It was Sarah who found the solution)",
                    "Focusing time/place: It was + Prepositional Phrase + that + Subject + Verb ... (e.g., It was in 2024 that we launched)"
                ]
            }
        ],
        "explanation_tr": "Türkçede vurgulanmak istenen öge cümlenin yükleminden hemen öncesine getirilir ('Bütçe açığına PAZARLAMA BÜTÇESİ neden oldu'). İngilizcede ise kelime sırası katı olduğu için cümlenin başı 'It was...' şeklinde bölünür ve odaklanan kelime hemen 'It was' sonrasına yerleştirilir.",
        "rules": [
            {
                "name": "It-Cleft Structure Rule",
                "pattern": "It + be (singular, matching tense) + Focused Element + that / who + Rest of Clause",
                "use_cases": [
                    "Pinpointing root causes during project retrospectives and forensic incident analysis",
                    "Clarifying misunderstandings and emphasizing key contributors in team evaluations"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Plural Verb Agreement Trap in It-Cleft",
                "description_tr": "Vurgulanan öge çoğul bile olsa cümlenin başındaki 'It' zamiri asla çoğul fiil almaz; 'It were the engineers' demek yanlıştır.",
                "trap_example": "It were the legacy dependencies that slowed down compilation.",
                "correction": "It was the legacy dependencies that slowed down compilation.",
                "key_difference_tr": "It-cleft yapısında açılış daima tekildir ('It is' veya 'It was'); vurgulanan öge çoğul olsa dahi değişmez."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Standard Sentence",
                "concept_b": "It-Cleft Sentence",
                "difference_en": "Standard sentences report neutral facts; It-clefts rectify assumptions or apply exclusive focus.",
                "difference_tr": "Standart cümle nötr bilgi aktarırken, It-cleft yanlış bir algıyı düzeltir veya özel bir ögeyi öne çıkarır.",
                "example_a": "A configuration typo caused the server downtime.",
                "example_b": "It was a configuration typo that caused the server downtime."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "It was in London where we met the investors.",
                "correct": "It was in London that we met the investors.",
                "explanation_en": "In standard It-cleft sentences, use 'that' rather than 'where' for prepositional adjuncts.",
                "explanation_tr": "It-cleft yapılarında yer edatları vurgulanırken 'where' değil 'that' tercih edilir."
            }
        ],
        "examples": [
            {
                "en": "It was the frontend team that first noticed the rendering discrepancy on mobile viewports.",
                "tr": "Mobil ekranlarda görselleştirme tutarsızlığını ilk fark eden ön yüz ekibiydi.",
                "context": "Post-release engineering retrospective",
                "register": "neutral_workplace",
                "highlighted_phrase": "It was the frontend team that"
            },
            {
                "en": "It was only after the internal security audit that management authorized two-factor authentication.",
                "tr": "Yönetim ancak dahili güvenlik denetiminden sonradır ki iki adımlı doğrulamaya onay verdi.",
                "context": "Compliance review meeting",
                "register": "formal_written",
                "highlighted_phrase": "It was only after the internal"
            },
            {
                "en": "It is clear documentation that transforms a promising open-source library into an industry standard.",
                "tr": "Umut vadeden bir açık kaynak kütüphanesini endüstri standardına dönüştüren şey net belgelendirmedir.",
                "context": "Technical keynote address",
                "register": "formal_written",
                "highlighted_phrase": "It is clear documentation that"
            },
            {
                "en": "It was not the database design but the network latency that degraded overall performance.",
                "tr": "Genel performansı düşüren şey veritabanı tasarımı değil, ağ gecikmesiydi.",
                "context": "Performance profiling report",
                "register": "neutral_workplace",
                "highlighted_phrase": "It was not the database design but"
            }
        ],
        "topic_tags": ["technology", "work-career"]
    },
    {
        "id": "grammar.b2.compound-relative-pronouns-whoever-whatever",
        "title": "Generalizing Relative Pronouns: 'Whoever, Whatever, Whichever, and Whenever'",
        "cefr_level": "B2",
        "category": "relative_and_participle_clauses",
        "summary_en": "Compound relative pronouns with '-ever' (whoever, whatever, whichever, whenever) mean 'any person who', 'anything that', or 'it doesn't matter what/when', introducing free relatives or concessive clauses.",
        "summary_tr": "'-ever' son eki alan bileşik ilgi zamirleri (whoever, whatever, whichever, whenever), 'her kim...', 'her ne...', 'hangi... olursa olsun' anlamlarına gelerek genelleştirici ve esnek yan cümleler kurar.",
        "explanation_en": [
            {
                "title": "Dual Grammatical Function: Free Relative vs. Concessive Adverbial",
                "content": "These words serve two primary functions in B2 prose. First, as nominal clauses functioning as subject or object: 'Whoever submits the best design will lead the project' (Meaning: Anyone who submits...). Second, as concessive adverbial clauses indicating that circumstances do not alter the main outcome: 'Whatever happens during the rollout, our support team will remain on standby'.",
                "patterns": [
                    "Nominal Subject: Whoever / Whatever + Verb ..., Main Verb (e.g., Whoever built this did a great job)",
                    "Concessive Clause: Whatever / Whenever + Subject + Verb, Main Clause (e.g., Whatever you decide, we support you)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Her kim olursa', 'Her ne olursa olsun', 'Ne zaman istersen' kalıplarıdır. Türk öğrencilerin en sık yaptığı hata, bu zamirlerin tekil özne olarak çekimlendiğini unutmaktır ('Whoever arrives early gets a desk' -> fiil tekil -s alır).",
        "rules": [
            {
                "name": "Singular Agreement in Nominal '-ever' Clauses",
                "pattern": "Whoever / Whichever + singular verb agreement in nominal subject role",
                "use_cases": [
                    "Formulating organizational policies, access rules, and open competitions",
                    "Expressing resilience and commitment regardless of external business conditions"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Plural Verb Trap with Whoever",
                "description_tr": "'Whoever' genel bir grubu kastetse de dilbilgisel olarak üçüncü tekil şahıs kabul edilir ve geniş zamanda fiile '-s' takısı getirilmelidir.",
                "trap_example": "Whoever approve the invoice must sign the physical ledger.",
                "correction": "Whoever APPROVES the invoice must sign the physical ledger.",
                "key_difference_tr": "'Whoever' öznesi tekil kabul edilir ve fiil tekil şahıs takısı alır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Whichever (Selection from a limited set)",
                "concept_b": "Whatever (Open-ended choice without limits)",
                "difference_en": "'Whichever' chooses between known, specified options; 'whatever' applies to an unrestricted universe of possibilities.",
                "difference_tr": "'Whichever' belirli ve sınırlı seçenekler arasından tercihte, 'whatever' ise sınırsız olasılıklarda kullanılır.",
                "example_a": "You can select whichever cloud provider best fits your latency needs (AWS, Azure, or GCP).",
                "example_b": "We will provide whatever assistance you need to complete the migration."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "No matter whoever is responsible, we must fix the outage.",
                "correct": "Whoever is responsible, we must fix the outage. / No matter WHO is responsible...",
                "explanation_en": "Do not combine 'no matter' with '-ever' pronouns; use 'no matter who' or simply 'whoever'.",
                "explanation_tr": "'No matter' kalıbı 'whoever' ile değil, sadece 'who' ile kullanılır ('No matter who')."
            }
        ],
        "examples": [
            {
                "en": "Whoever discovers a reproducible security vulnerability will receive a bug bounty reward.",
                "tr": "Tekrarlanabilir bir güvenlik açığı keşfeden her kim olursa bir hata ödülü alacaktır.",
                "context": "Corporate bug bounty policy",
                "register": "formal_written",
                "highlighted_phrase": "Whoever discovers a reproducible"
            },
            {
                "en": "Whatever decision the executive board reaches today will shape our technical roadmap for years.",
                "tr": "Yönetim kurulunun bugün alacağı karar ne olursa olsun, teknik yol haritamızı yıllarca şekillendirecektir.",
                "context": "Company-wide all-hands briefing",
                "register": "neutral_workplace",
                "highlighted_phrase": "Whatever decision the executive board"
            },
            {
                "en": "Feel free to ping our engineering team on Slack whenever you need technical clarification.",
                "tr": "Teknik bir açıklamaya ne zaman ihtiyaç duyarsanız Slack üzerinden ekibimize yazmaktan çekinmeyin.",
                "context": "Client onboarding welcome message",
                "register": "neutral_workplace",
                "highlighted_phrase": "whenever you need technical"
            },
            {
                "en": "Choose whichever framework your development team feels most proficient with.",
                "tr": "Geliştirme ekibinizin kendisini en yetkin hissettiği çerçeve hangisiyse onu seçin.",
                "context": "Architecture recommendation advice",
                "register": "neutral_workplace",
                "highlighted_phrase": "whichever framework your development"
            }
        ],
        "topic_tags": ["work-career", "technology"]
    },
    {
        "id": "grammar.b2.adverbial-clauses-of-manner-as-if-as-though",
        "title": "Clauses of Manner and Hypothetical Comparison: 'As if' and 'As though'",
        "cefr_level": "B2",
        "category": "conditionals_and_hypotheticals",
        "summary_en": "'As if' and 'as though' describe impressions and comparisons; when describing an unreal or improbable situation, the verb shifts back one tense (unreal past or 'were').",
        "summary_tr": "'As if' ve 'as though' (-mış gibi / sanki), durum ve izlenimleri anlatır; gerçek dışı veya varsayımsal durumlarda zaman bir derece geriye kayar (Unreal Past / were).",
        "explanation_en": [
            {
                "title": "Real Impression vs. Counterfactual Hypothetical",
                "content": "When an observation is likely true based on evidence, use normal tenses: 'He looks as if he is exhausted' (he probably is). When the comparison is contrary to known facts or purely imaginative, use unreal past tenses: 'She talks as if she owned the company' (she clearly doesn't). In formal English, 'were' can replace 'was' for all persons in unreal comparisons: 'He acts as though he were the sole decision-maker'.",
                "patterns": [
                    "Likely true: Subject + Verb + as if / as though + Present Tense (e.g., It looks as if it is raining)",
                    "Counterfactual: Subject + Verb + as if / as though + Past Tense / were (e.g., He acts as if he knew everything)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'sanki ...-mış gibi' ifadesidir. Gerçek bir izlenim aktarılıyorsa geniş zaman ('looks as if it is'), tamamen hayali veya gerçeğe aykırı bir benzetme yapılıyorsa geçmiş zaman ('speaks as if he were') kullanılır.",
        "rules": [
            {
                "name": "Hypothetical Tense Shift in 'As if' Clauses",
                "pattern": "Unreal comparison -> as if / as though + Past Simple / Subjunctive 'were'",
                "use_cases": [
                    "Critiquing organizational behavior, unrealistic vendor promises, and workplace attitudes",
                    "Describing physical appearances, atmospheric impressions, and simulated behaviors"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Overuse of 'Like' in Formal Writing",
                "description_tr": "Konuşma dilinde 'like' yaygınlaşsa da resmi ve profesyonel yazışmalarda 'like he was' yerine 'as if / as though' tercih edilmelidir.",
                "trap_example": "The manager spoke like the budget was unlimited.",
                "correction": "The manager spoke as if the budget were unlimited.",
                "key_difference_tr": "Yazılı iş İngilizcesinde varsayımsal benzetmeler için 'like' yerine 'as if' veya 'as though' kullanılır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "as if + present (Probable reality)",
                "concept_b": "as if + past (Counterfactual assumption)",
                "difference_en": "Present tense signals genuine probability based on signs; past tense signals an impossible or exaggerated analogy.",
                "difference_tr": "Geniş zaman somut kanıtlara dayalı olası bir durumu, geçmiş zaman ise gerçeğe aykırı bir benzetmeyi ifade eder.",
                "example_a": "The client sounds as if they are satisfied with our delivery.",
                "example_b": "The contractor behaves as though he were an equity partner in the firm."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "She acts as if she is knowing all the secrets.",
                "correct": "She acts as if she knew all the secrets.",
                "explanation_en": "Counterfactual 'as if' clauses take the simple past tense, not progressive stative verbs.",
                "explanation_tr": "Gerçek dışı 'as if' cümleciğinde durum fiilleriyle continuous değil, simple past kullanılır."
            }
        ],
        "examples": [
            {
                "en": "The legacy application runs so sluggishly that it feels as if it were running on dial-up hardware.",
                "tr": "Eski uygulama o kadar yavaş çalışıyor ki sanki çevirmeli ağ donanımında çalışıyormuş gibi hissettiriyor.",
                "context": "Software performance diagnostic notes",
                "register": "neutral_workplace",
                "highlighted_phrase": "as if it were running"
            },
            {
                "en": "The financial market reacted as though interest rates had already been lowered by the central bank.",
                "tr": "Finans piyasası, faiz oranları merkez bankası tarafından zaten düşürülmüş gibi tepki verdi.",
                "context": "Macroeconomic quarterly newsletter",
                "register": "formal_written",
                "highlighted_phrase": "as though interest rates had"
            },
            {
                "en": "Judging by the empty parking lot, it looks as if the entire team has left for the weekend.",
                "tr": "Boş otoparka bakılırsa, tüm ekip hafta sonu için ayrılmış gibi görünüyor.",
                "context": "Friday afternoon office observation",
                "register": "informal_spoken",
                "highlighted_phrase": "looks as if the entire"
            },
            {
                "en": "The vendor negotiated aggressively, acting as though our firm had no viable alternative suppliers.",
                "tr": "Tedarikçi, şirketimizin hiçbir uygulanabilir alternatif tedarikçisi yokmuş gibi davranarak agresif bir müzakere yürüttü.",
                "context": "Procurement negotiation debrief",
                "register": "neutral_workplace",
                "highlighted_phrase": "acting as though our firm had"
            }
        ],
        "topic_tags": ["business", "communication"]
    },
    {
        "id": "grammar.b2.modals-of-deduction-present-continuous-must-be-doing",
        "title": "Aspectual Modals of Deduction: 'Must be doing', 'Can't be doing', and 'Might be doing'",
        "cefr_level": "B2",
        "category": "modals_and_semi_modals",
        "summary_en": "Modals of deduction combine with the continuous aspect (modal + be + -ing) to express logical deductions about ongoing actions occurring at the present moment.",
        "summary_tr": "Çıkarım modalları şimdiki zaman fiilimsisiyle (modal + be + -ing) birleşerek, şu anda gerçekleşmekte olan eylemler hakkında mantıksal çıkarımları (kesinlik, imkansızlık, ihtimal) ifade eder.",
        "explanation_en": [
            {
                "title": "Logical Deduction with Continuous Aspect",
                "content": "When making an inference about an activity happening right now, use 'Must / Can't / Might / Could + be + verb-ing'. 'Must be doing' expresses near-certain deduction based on evidence ('Her laptop is open, so she must be working'). 'Can't be doing' expresses logical impossibility ('His status is offline, so he can't be attending the webinar'). 'Might / could be doing' expresses tentative possibility.",
                "patterns": [
                    "Near certainty: Subject + must be + Verb-ing (e.g., They must be discussing the contract now)",
                    "Impossibility: Subject + can't be + Verb-ing (e.g., He can't be sleeping at this hour)",
                    "Possibility: Subject + might/could be + Verb-ing (e.g., The server might be restarting)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Şu an çalışıyor olmalı' veya 'Toplantıda konuşuyor olamaz' çıkarımlarıdır. En büyük yanılgı, imkansızlık çıkarımı yaparken 'must not' kullanmaktır. İngilizcede 'şu an bunu yapıyor olamaz' mantıksal çıkarımı kesinlikle 'can't be doing' ile yapılır; 'must not' sadece yasaklama belirtir.",
        "rules": [
            {
                "name": "Continuous Modal Deduction Polarity",
                "pattern": "Positive Certainty: MUST be doing | Negative Impossibility: CAN'T be doing (NOT mustn't)",
                "use_cases": [
                    "Inferring ongoing colleague availability, system operations, and background jobs",
                    "Deducing current user actions and troubleshooting ongoing network traffic"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Must Not for Negative Deduction Trap",
                "description_tr": "Şu an devam eden bir eylemin mantıken imkansız olduğunu söylerken 'must not be doing' demek 'yapmaması gerekir' (yasak) anlamı doğurur.",
                "trap_example": "The database is completely isolated; it mustn't be receiving external requests.",
                "correction": "The database is completely isolated; it CAN'T be receiving external requests.",
                "key_difference_tr": "Mantıksal imkansızlık çıkarımı 'can't be doing' ile yapılır; 'mustn't' ise yasaklama bildirir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "must do (Habitual deduction or obligation)",
                "concept_b": "must be doing (Ongoing activity deduction right now)",
                "difference_en": "'Must do' refers to general fact or necessity; 'must be doing' specifically targets an activity unfolding at the moment of speaking.",
                "difference_tr": "'Must do' genel gerekliliği ya da durumu; 'must be doing' ise tam şu anda süren eyleme dair çıkarımı belirtir.",
                "example_a": "Engineers must wear protective helmets on the factory floor.",
                "example_b": "Look at the busy dashboard; the engineers must be deploying the new build right now."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "She is not answering her phone; she can be driving.",
                "correct": "She is not answering her phone; she MIGHT / COULD be driving.",
                "explanation_en": "Do not use 'can' for affirmative speculative deductions; use 'might', 'may', or 'could'.",
                "explanation_tr": "Olumlu olasılık çıkarımlarında 'can' kullanılmaz; 'might', 'may' veya 'could' kullanılır."
            }
        ],
        "examples": [
            {
                "en": "The CEO's calendar is completely blocked this morning; she must be conducting executive interviews.",
                "tr": "İcra kurulu başkanının takvimi bu sabah tamamen kapalı; üst düzey mülakatlar yapıyor olmalı.",
                "context": "Executive assistant scheduling conversation",
                "register": "neutral_workplace",
                "highlighted_phrase": "must be conducting executive"
            },
            {
                "en": "The telemetry indicators are flatlining; the database service can't be processing transactions.",
                "tr": "Telemetri göstergeleri sıfırlandı; veritabanı servisi şu anda işlemleri işliyor olamaz.",
                "context": "Site reliability engineering outage channel",
                "register": "neutral_workplace",
                "highlighted_phrase": "can't be processing transactions"
            },
            {
                "en": "Our lead security analyst is not at her desk; she could be attending the compliance committee.",
                "tr": "Baş güvenlik analistimiz masasında değil; uyumluluk komitesine katılıyor olabilir.",
                "context": "Office desk inquiry",
                "register": "neutral_workplace",
                "highlighted_phrase": "could be attending the compliance"
            },
            {
                "en": "The automated test suite is taking twice as long as usual; it might be rebuilding the cache.",
                "tr": "Otomatik test paketi her zamankinden iki kat daha uzun sürüyor; önbelleği yeniden oluşturuyor olabilir.",
                "context": "Developer chat during build pipeline run",
                "register": "neutral_workplace",
                "highlighted_phrase": "might be rebuilding the cache"
            }
        ],
        "topic_tags": ["technology", "work-career"]
    },
    {
        "id": "grammar.b2.prepositional-phrases-specifying-aspect-in-terms-of",
        "title": "Framing and Viewpoint Phrases: 'In terms of', 'With regard to', and 'As for'",
        "cefr_level": "B2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Complex prepositional phrases establish analytical scope and viewpoint, guiding readers through comparative business reports and technical evaluations.",
        "summary_tr": "Karmaşık edat öbekleri (In terms of, With regard to, As for vb.), teknik rapor ve iş değerlendirmelerinde bakış açısını sınırlandırarak konuyu belirli bir çerçeveye oturtur ('... açısından', '... hususunda').",
        "explanation_en": [
            {
                "title": "Establishing Focus Boundaries",
                "content": "'In terms of' isolates a specific dimension or metric for evaluation (e.g., performance, cost, security): 'In terms of throughput, Solution A wins'. 'With regard to' (or 'in regard to') introduces a relevant topic for official consideration: 'With regard to the warranty, our policy remains unchanged'. 'As for' shifts thematic focus to a distinct person or item already mentioned: 'As for the timeline, we anticipate minor slippage'.",
                "patterns": [
                    "Evaluation dimension: In terms of + Noun / -ing (e.g., In terms of scalability)",
                    "Formal topic introduction: With regard to / In respect of + Noun (e.g., With regard to licensing)",
                    "Topic shift: As for + Noun, Subject + Verb (e.g., As for the hardware costs, they are fixed)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki '... açısından / bakımından', '... konusunda' ifadeleridir. En sık yapılan hata 'with regards to' şeklinde çoğul 's' eklemektir; deyimsel doğru form 'with regard to' (tekil) şeklindedir.",
        "rules": [
            {
                "name": "Viewpoint Phrase Invariable Forms",
                "pattern": "in terms of + Noun | with regard to + Noun (NO plural 's')",
                "use_cases": [
                    "Structuring comparative technical assessments and vendor selection matrixes",
                    "Transitioning between topics in formal executive memoranda and performance audits"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "With Regards To Error",
                "description_tr": "Mektup sonundaki 'Kind regards' selamlaşmasından etkilenerek 'with regard to' yapısına çoğul '-s' eklemek profesyonel metinlerde çok yaygın bir biçimsel hatadır.",
                "trap_example": "With regards to your inquiry regarding software support, we offer 24/7 coverage.",
                "correction": "With REGARD to your inquiry regarding software support, we offer 24/7 coverage.",
                "key_difference_tr": "Edat öbeğinde 'regard' tekil kalır: 'with regard to' veya 'in regard to'."
            }
        ],
        "contrasts": [
            {
                "concept_a": "In terms of (Evaluative dimension)",
                "concept_b": "As for (Thematic focus shift)",
                "difference_en": "'In terms of' measures along a metric; 'As for' changes the subject to an adjacent issue.",
                "difference_tr": "'In terms of' bir ölçüte göre değerlendirme yaparken, 'As for' farklı bir konuya geçişi sağlar.",
                "example_a": "In terms of energy efficiency, the new processors outperform the old architecture.",
                "example_b": "The software is ready. As for the user manual, it will be completed by tomorrow."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "In term of speed, this algorithm is superior.",
                "correct": "In TERMS of speed, this algorithm is superior.",
                "explanation_en": "The fixed idiom is always plural 'in terms of', never singular 'in term of'.",
                "explanation_tr": "Deyim daima çoğuldur: 'in terms of'."
            }
        ],
        "examples": [
            {
                "en": "In terms of memory consumption, microservices demand greater total overhead than monolithic applications.",
                "tr": "Bellek tüketimi açısından mikro servisler, monolitik uygulamalardan daha fazla toplam ek yük gerektirir.",
                "context": "System architecture trade-off evaluation",
                "register": "formal_written",
                "highlighted_phrase": "In terms of memory consumption"
            },
            {
                "en": "With regard to compliance with GDPR regulations, our legal counsel has drafted comprehensive guidelines.",
                "tr": "GDPR düzenlemelerine uyum hususunda, hukuk danışmanımız kapsamlı yönergeler hazırladı.",
                "context": "Compliance committee summary",
                "register": "formal_written",
                "highlighted_phrase": "With regard to compliance"
            },
            {
                "en": "The core application code is stable; as for the third-party integrations, further testing is necessary.",
                "tr": "Ana uygulama kodu kararlı; üçüncü taraf entegrasyonlarına gelince, daha fazla test yapılması gerekiyor.",
                "context": "Sprint release readiness report",
                "register": "neutral_workplace",
                "highlighted_phrase": "as for the third-party"
            },
            {
                "en": "In terms of return on investment, automating data pipelines yielded immediate operational benefits.",
                "tr": "Yatırım getirisi bakımından veri hatlarını otomatikleştirmek anında operasyonel faydalar sağladı.",
                "context": "Executive business case review",
                "register": "formal_written",
                "highlighted_phrase": "In terms of return on"
            }
        ],
        "topic_tags": ["business", "technology"]
    },
    {
        "id": "grammar.b2.verb-noun-collocational-grammar-light-verbs",
        "title": "Support Verb Constructions: 'Take action', 'Make an effort', and 'Bear in mind'",
        "cefr_level": "B2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Light verb constructions pair semantically bleached verbs (make, take, give, bear, draw) with specific deverbal nouns, creating professional and idiomatic expressions.",
        "summary_tr": "Hafif fiil yapıları (Support Verb Constructions), 'make, take, give, bear' gibi fiilleri belirli isimlerle eşleştirerek profesyonel ve deyimsel ifadeler oluşturur (take action, make an effort, bear in mind vb.).",
        "explanation_en": [
            {
                "title": "Collocational Precision with Delexical Verbs",
                "content": "Rather than using simple verbs alone ('act', 'try', 'remember'), advanced workplace English favors nominalized support verb constructions: 'take action' (act), 'make an effort' (try), 'bear in mind' (remember/consider), 'give consideration to' (consider), 'draw a conclusion' (conclude), 'reach an agreement' (agree). Using the wrong support verb (e.g., 'do a decision' instead of 'make a decision') breaks idiomatic fluency immediately.",
                "patterns": [
                    "make + decision / effort / contribution / progress / appointment",
                    "take + action / steps / measures / into account / precedence",
                    "bear + in mind | draw + attention to / conclusion | pay + attention"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'karar almak', 'adım atmak', 'göz önünde bulundurmak' gibi kalıplaşmış fiil-isim birliktelikleridir. Türk öğrencilerin en yaygın hatası, 'make' ile 'do' ayrımını karıştırmak veya 'take a decision' (Fransızca/Türkçe etkisi) yerine 'make a decision' demeyi unutmaktır.",
        "rules": [
            {
                "name": "Fixed Delexical Verb Match",
                "pattern": "Verb + Noun collocation fixed pairs (make a choice, take steps, give guidance)",
                "use_cases": [
                    "Elevating conversational vocabulary into professional executive register",
                    "Formulating policy directives, team resolutions, and strategic initiatives"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Do a Decision / Take a Decision Trap",
                "description_tr": "Türkçedeki 'karar almak' ifadesini 'take a decision' veya 'yapmak' sanıp 'do a decision' şeklinde aktarmak yaygın bir L1 transfer hatasıdır; standart İngilizce 'make a decision' gerektirir.",
                "trap_example": "The executive committee must take a quick decision regarding the acquisition.",
                "correction": "The executive committee must MAKE a quick decision regarding the acquisition.",
                "key_difference_tr": "İngilizcede kararlar 'make' fiili ile üretilir: 'make a decision'."
            }
        ],
        "contrasts": [
            {
                "concept_a": "decide (Simple lexical verb)",
                "concept_b": "make a decision (Support verb construction)",
                "difference_en": "'Decide' is direct; 'make a decision' allows modification with adjectives ('make a prompt, unanimous decision') for greater communicative nuance.",
                "difference_tr": "'Decide' yalın bir eylemdir; 'make a decision' ise araya sıfat alarak zenginleştirilebilir ('make an informed decision').",
                "example_a": "We decided to pause the project.",
                "example_b": "We made a calculated decision to pause the project."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "Keep in your mind that the deadline is Thursday.",
                "correct": "Bear in mind that the deadline is Thursday. / Keep in mind that...",
                "explanation_en": "The fixed idiom is 'bear in mind' or 'keep in mind' without possessive pronouns.",
                "explanation_tr": "Deyim 'bear in mind' veya 'keep in mind' şeklindedir; 'in your mind' denmez."
            }
        ],
        "examples": [
            {
                "en": "Management must take prompt action to mitigate cybersecurity exposure across remote workstations.",
                "tr": "Yönetim, uzaktaki iş istasyonlarındaki siber güvenlik açıklarını azaltmak için derhal harekete geçmelidir.",
                "context": "Cybersecurity posture evaluation",
                "register": "formal_written",
                "highlighted_phrase": "take prompt action"
            },
            {
                "en": "When estimating project milestones, always bear in mind the upcoming public holidays.",
                "tr": "Proje kilometre taşlarını tahmin ederken, yaklaşan resmi tatilleri daima göz önünde bulundurun.",
                "context": "Project planning guidance",
                "register": "neutral_workplace",
                "highlighted_phrase": "bear in mind"
            },
            {
                "en": "Both organizations made a concerted effort to align their technical standards before launch.",
                "tr": "Her iki kuruluş da lansmandan önce teknik standartlarını uyumlu hale getirmek için yoğun bir çaba sarf etti.",
                "context": "Joint venture press release",
                "register": "formal_written",
                "highlighted_phrase": "made a concerted effort"
            },
            {
                "en": "Our financial analysts drew a clear conclusion based on three consecutive quarters of revenue growth.",
                "tr": "Finansal analistlerimiz, üst üste üç çeyrek yaşanan gelir artışına dayanarak net bir sonuca ulaştı.",
                "context": "Market research investor memo",
                "register": "formal_written",
                "highlighted_phrase": "drew a clear conclusion"
            }
        ],
        "topic_tags": ["business", "work-career"]
    },
    {
        "id": "grammar.b2.adverbs-of-degree-and-grading-completely-vs-fairly",
        "title": "Adverbs of Degree and Grading: Extreme vs. Gradable Adjectives",
        "cefr_level": "B2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Adverbs of degree collocate strictly with adjective types: gradable adjectives take modifying adverbs (fairly, very, extremely), whereas ungradable/extreme adjectives require non-gradable intensifiers (completely, utterly, absolutely).",
        "summary_tr": "Derecelendirme zarfları sıfat türüne göre kesin kurallarla eşleşir: Derecelendirilebilir sıfatlar 'very, fairly, extremely' alırken; mutlak/uç sıfatlar 'completely, absolutely, utterly' zarflarını alır.",
        "explanation_en": [
            {
                "title": "Gradable vs. Extreme / Absolute Adjectives",
                "content": "Gradable adjectives exist on a continuous spectrum (cold, tired, expensive, important) and pair with grading adverbs: 'very cold', 'extremely expensive', 'slightly tired'. Ungradable adjectives represent absolute states or extremes (freezing, exhausted, priceless, essential, impossible) and cannot take 'very'. They pair exclusively with absolute intensifiers: 'absolutely freezing', 'completely impossible', 'utterly exhausted'. 'Quite' alters its meaning: with gradable adjectives it means 'fairly' ('quite good'); with extreme adjectives it means 'completely' ('quite brilliant').",
                "patterns": [
                    "Gradable: very / extremely / rather + Gradable Adjective (e.g., extremely difficult)",
                    "Extreme: absolutely / utterly / completely + Extreme Adjective (e.g., completely unacceptable)",
                    "Dual-use: really works with both (e.g., really tired / really exhausted)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'çok yorgun' da 'çok bitkin' de denilebilir. Ancak İngilizcede 'very exhausted' demek kural hatasıdır; çünkü 'exhausted' zaten 'aşırı yorgun' demektir ve 'very' ile derecelendirilemez; 'absolutely exhausted' veya 'completely exhausted' denmelidir.",
        "rules": [
            {
                "name": "Adjective Degree Collocation Rule",
                "pattern": "very + gradable (very good, NOT very perfect) vs. absolutely + extreme (absolutely perfect, NOT very perfect)",
                "use_cases": [
                    "Calibrating executive assessments, risk severity, and quality evaluations",
                    "Expressing precise personal reactions and nuanced operational critiques"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Very with Extreme Adjectives Trap",
                "description_tr": "Türkçedeki 'çok mükemmel' veya 'çok imkansız' kullanımını İngilizceye doğrudan 'very perfect' veya 'very impossible' olarak çevirmek bariz bir hatadır.",
                "trap_example": "The zero-downtime database migration was very impossible under those budget constraints.",
                "correction": "The zero-downtime database migration was COMPLETELY impossible under those budget constraints.",
                "key_difference_tr": "'Impossible' mutlak bir sıfattır; derecelendirilemez, 'completely' veya 'totally' alır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "very + gradable (Continuous scale)",
                "concept_b": "absolutely + extreme (Binary absolute state)",
                "difference_en": "'Very' increments an attribute; 'absolutely' confirms that an extreme limit has been reached.",
                "difference_tr": "'Very' kademeli bir artış bildirir; 'absolutely' ise uç noktaya ulaşıldığını teyit eder.",
                "example_a": "The latency is very noticeable to end users.",
                "example_b": "The data corruption is absolutely catastrophic for the institution."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "Our client was very delighted with the results.",
                "correct": "Our client was absolutely delighted with the results.",
                "explanation_en": "'Delighted' is an extreme adjective meaning 'very pleased'; it takes 'absolutely', not 'very'.",
                "explanation_tr": "'Delighted' uç bir sıfattır, 'very' ile kullanılmaz; 'absolutely' alır."
            }
        ],
        "examples": [
            {
                "en": "Operating without automated backups in a production environment is utterly irresponsible.",
                "tr": "Canlı ortamda otomatik yedekleme olmadan çalışmak tamamen sorumsuzcadır.",
                "context": "Cloud architecture review",
                "register": "formal_written",
                "highlighted_phrase": "utterly irresponsible"
            },
            {
                "en": "The new analytics dashboard is extremely intuitive for non-technical users.",
                "tr": "Yeni analiz panosu, teknik olmayan kullanıcılar için son derece sezgiseldir.",
                "context": "User testing summary",
                "register": "neutral_workplace",
                "highlighted_phrase": "extremely intuitive"
            },
            {
                "en": "Completing the hardware redesign within two weeks is practically impossible.",
                "tr": "Donanım yeniden tasarımını iki hafta içinde tamamlamak neredeyse imkansızdır.",
                "context": "Engineering feasibility meeting",
                "register": "neutral_workplace",
                "highlighted_phrase": "practically impossible"
            },
            {
                "en": "We were absolutely thrilled to learn that our patent application was approved.",
                "tr": "Patent başvurumuzun onaylandığını öğrenmekten kesinlikle büyük mutluluk duyduk.",
                "context": "Company announcement",
                "register": "formal_written",
                "highlighted_phrase": "absolutely thrilled"
            }
        ],
        "topic_tags": ["communication", "work-career"]
    },
    {
        "id": "grammar.b2.passive-gerunds-and-infinitives",
        "title": "Passive Complementation: Passive Gerunds ('Being done') and Passive Infinitives ('To be done')",
        "cefr_level": "B2",
        "category": "passive_and_causative",
        "summary_en": "Passive gerunds ('being + past participle') and passive infinitives ('to be + past participle') allow learners to describe receiving actions in nominal and non-finite positions.",
        "summary_tr": "Edilgen fiilimsiler ('being + V3') ve edilgen mastarlar ('to be + V3'), eylemi yapan değil eyleme maruz kalan ögeyi fiilimsi ve mastar pozisyonlarında ifade eder.",
        "explanation_en": [
            {
                "title": "Forming Non-Finite Passive Verb Forms",
                "content": "When a verb requiring a gerund or infinitive expresses a passive experience rather than an active deed, the complement must be passivized. Verbs followed by gerunds become 'being + past participle': 'He hates being interrupted' (active: He hates people interrupting him). Verbs followed by infinitives become 'to be + past participle': 'The document needs to be reviewed' (active: Someone needs to review the document). For past passive events, use perfect passive forms: 'having been rejected', 'to have been informed'.",
                "patterns": [
                    "Passive Gerund: Verb + being + Past Participle (e.g., avoid being tracked)",
                    "Passive Infinitive: Verb + to be + Past Participle (e.g., expect to be promoted)",
                    "Perfect Passive Gerund: having been + Past Participle (e.g., despite having been warned)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'yapılmak istemek' veya 'kesintiye uğramaktan nefret etmek' edilgen eylemleridir. Türk öğrencilerin en sık yaptığı hata, fiilin edilgen olması gerektiğini unutup etken mastar kurmasıdır ('The bug needs to fix' yerine 'The bug needs to BE FIXED' denmelidir).",
        "rules": [
            {
                "name": "Non-Finite Passive Voice Matching",
                "pattern": "Active Gerund (doing) -> Passive Gerund (being done) | Active Infinitive (to do) -> Passive Infinitive (to be done)",
                "use_cases": [
                    "Describing workplace compliance, security auditing, and system monitoring",
                    "Formulating impersonal action items, tickets, and organizational processes"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Active Complement for Passive Subject Trap",
                "description_tr": "Özne bir nesne veya işlem olduğunda fiili etken bırakıp 'The application needs to update' demek hatalıdır; 'needs to be updated' olmalıdır.",
                "trap_example": "The database configuration needs to change before migration.",
                "correction": "The database configuration needs TO BE CHANGED before migration.",
                "key_difference_tr": "Veritabanı kendi kendini değiştiremeyeceği için edilgen mastar ('to be changed') kullanılmalıdır."
            }
        ],
        "contrasts": [
            {
                "concept_a": "Active Gerund (Doing)",
                "concept_b": "Passive Gerund (Being done)",
                "difference_en": "Active gerund indicates the subject performs the act; passive gerund indicates the subject receives it.",
                "difference_tr": "Etken gerund eylemi yapanı, edilgen gerund ise eyleme maruz kalanı belirtir.",
                "example_a": "I appreciate your evaluating my application.",
                "example_b": "I appreciate being evaluated on objective metrics."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "She was afraid of firing by the management.",
                "correct": "She was afraid of BEING FIRED by the management.",
                "explanation_en": "The subject is receiving the termination, requiring the passive gerund 'being fired'.",
                "explanation_tr": "Eyleme maruz kalındığı için 'firing' değil, edilgen gerund olan 'being fired' kullanılır."
            }
        ],
        "examples": [
            {
                "en": "All network traffic within the corporate cloud is subject to being monitored by security protocols.",
                "tr": "Kurumsal buluttaki tüm ağ trafiği, güvenlik protokolleri tarafından izlenmeye tabidir.",
                "context": "Corporate IT acceptable use policy",
                "register": "formal_written",
                "highlighted_phrase": "being monitored by"
            },
            {
                "en": "The revised terms of service must be approved by legal counsel before public release.",
                "tr": "Revize edilen hizmet şartları kamuya açıklanmadan önce hukuk müşavirliği tarafından onaylanmalıdır.",
                "context": "Compliance release gate document",
                "register": "formal_written",
                "highlighted_phrase": "must be approved by"
            },
            {
                "en": "No engineer enjoys being interrupted repeatedly during deep focus coding sessions.",
                "tr": "Hiçbir mühendis derin odaklanma gerektiren kodlama seanslarında defalarca bölünmekten hoşlanmaz.",
                "context": "Workplace productivity blog post",
                "register": "neutral_workplace",
                "highlighted_phrase": "enjoys being interrupted"
            },
            {
                "en": "Having been notified of the outage promptly, our on-call engineers restored operations quickly.",
                "tr": "Kesintiden derhal haberdar edilmiş olan nöbetçi mühendislerimiz, operasyonu hızla geri yükledi.",
                "context": "Post-incident customer communication",
                "register": "formal_written",
                "highlighted_phrase": "Having been notified of"
            }
        ],
        "topic_tags": ["work-career", "technology"]
    },
    {
        "id": "grammar.b2.contrast-and-comparison-whereas-while-unlike",
        "title": "Contrast and Comparison: 'Whereas', 'While', and 'Unlike' in Analytical Writing",
        "cefr_level": "B2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "'Whereas' and 'while' contrast two parallel facts in subordinate clauses, while 'unlike' acts as a preposition preceding a noun phrase to introduce contrastive traits.",
        "summary_tr": "'Whereas' ve 'while' (-iken / oysa ki) iki paralel gerçeği yan cümlelerle karşılaştırırken; 'unlike' (-in aksine) bir isim öbeğinin önüne gelerek zıt özellikleri vurgular.",
        "explanation_en": [
            {
                "title": "Subordinating Conjunctions vs. Prepositional Contrast",
                "content": "'Whereas' and 'while' are conjunctions followed by a complete clause (Subject + Verb) that balance two contrasting facts without necessarily implying conflict: 'Monoliths are simpler to deploy, whereas microservices scale independently'. 'Unlike' is a preposition followed only by a noun phrase or pronoun: 'Unlike monolithic architectures, microservices require container orchestration'. A comma typically separates the contrasting elements.",
                "patterns": [
                    "Conjunction contrast: Clause 1, whereas / while + Clause 2 (e.g., Sales rose in Asia, while they fell in Europe)",
                    "Prepositional contrast: Unlike + Noun Phrase, Subject + Verb (e.g., Unlike our competitor, we offer 24/7 support)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'buna karşılık / oysa' (whereas/while) ve '-in aksine' (unlike) kalıplarıdır. En büyük hata, 'unlike' sözcüğünden sonra tam cümle getirmeye çalışmaktır; 'unlike' bir edattır ve ardından sadece isim veya zamir gelir ('Unlike monoliths', NOT 'Unlike monoliths are').",
        "rules": [
            {
                "name": "Structural Differentiation of Contrast Linkers",
                "pattern": "whereas/while + Subject + Verb vs. unlike + Noun Phrase (NO verb)",
                "use_cases": [
                    "Writing comparative product analyses, benchmark evaluations, and business reports",
                    "Comparing performance metrics across quarters, cohorts, and demographic groups"
                ]
            }
        ],
        "turkish_traps": [
            {
                "name": "Unlike Followed by a Clause Trap",
                "description_tr": "'Unlike' kelimesini bağlaç sanıp arkasından özne ve yüklem getirmek İngilizcede dilbilgisel bir hatadır.",
                "trap_example": "Unlike traditional servers require physical maintenance, cloud instances are virtual.",
                "correction": "Unlike traditional servers, cloud instances are virtual and require no physical maintenance.",
                "key_difference_tr": "'Unlike' sadece isim öbeği alır; karşılaştırılan yargı ana cümlenin yükleminde verilir."
            }
        ],
        "contrasts": [
            {
                "concept_a": "whereas (Conjunction linking clauses)",
                "concept_b": "unlike (Preposition preceding a noun)",
                "difference_en": "'Whereas' connects two full subject-verb propositions; 'unlike' modifies a noun by antithesis.",
                "difference_tr": "'Whereas' iki tam cümleyi birbirine bağlar; 'unlike' ise bir ismin önüne gelerek zıtlık kurar.",
                "example_a": "Product A focuses on speed, whereas Product B prioritizes extensive customization.",
                "example_b": "Unlike Product A, Product B offers comprehensive custom settings."
            }
        ],
        "common_mistakes": [
            {
                "incorrect": "In contrast to our competitor has a large sales team, we rely on product-led growth.",
                "correct": "In contrast to our competitor, who has a large sales team, we rely on product-led growth.",
                "explanation_en": "'In contrast to' and 'unlike' require a noun phrase complement, not an uncoordinated finite clause.",
                "explanation_tr": "'In contrast to' ve 'unlike' arkasından doğrudan tam cümle alamaz."
            }
        ],
        "examples": [
            {
                "en": "Relational databases enforce strict schema rigidity, whereas document stores offer flexible data models.",
                "tr": "İlişkisel veritabanları katı şema zorunluluğu uygularken, doküman depoları esnek veri modelleri sunar.",
                "context": "Database architecture trade-off report",
                "register": "formal_written",
                "highlighted_phrase": "whereas document stores offer"
            },
            {
                "en": "Unlike traditional on-premise hardware, cloud infrastructure scales dynamically according to real-time load.",
                "tr": "Geleneksel şirket içi donanımın aksine, bulut altyapısı gerçek zamanlı yüke göre dinamik olarak ölçeklenir.",
                "context": "Cloud migration business case",
                "register": "formal_written",
                "highlighted_phrase": "Unlike traditional on-premise hardware"
            },
            {
                "en": "European markets experienced steady recovery in Q2, while North American revenues remained flat.",
                "tr": "Avrupa pazarları 2. çeyrekte istikrarlı bir toparlanma yaşarken, Kuzey Amerika gelirleri yatay seyretti.",
                "context": "Quarterly earnings report summary",
                "register": "formal_written",
                "highlighted_phrase": "while North American revenues"
            },
            {
                "en": "Unlike our previous monolithic platform, our current microservices architecture isolates faults effectively.",
                "tr": "Önceki monolitik platformumuzun aksine, mevcut mikro servis mimarimiz hataları etkin bir şekilde izole eder.",
                "context": "Engineering retrospective article",
                "register": "formal_written",
                "highlighted_phrase": "Unlike our previous monolithic"
            }
        ],
        "topic_tags": ["technology", "business"]
    }
]
